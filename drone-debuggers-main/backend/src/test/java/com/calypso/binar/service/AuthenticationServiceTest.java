package com.calypso.binar.service;

import com.calypso.binar.SignInRequest;
import com.calypso.binar.SignInResponse;
import com.calypso.binar.model.Role;
import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.PasswordResetDTO;
import com.calypso.binar.repository.UserRepository;
import com.calypso.binar.security.JwtService;
import com.calypso.binar.service.exception.AccountLockedException;
import com.calypso.binar.service.exception.FailedAuthenticationException;
import com.calypso.binar.service.exception.UserNotFoundException;
import com.calypso.binar.service.validation.AccountValidatorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private AccountValidatorService validatorService;


    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService underTest;


    @BeforeEach
    public void setup() {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.createEmptyContext();
    }

    /**
     * Test to verify that the active user is correctly returned from the SecurityContext.
     * Ensures that the user set in the SecurityContext is retrieved correctly.
     */
    @Test
    public void getActiveUser_returnsUserFromSecurityContext_whenUserIsAuthenticated() {
        //given a user in SecurityContext
        User user = new User();
        user.setEmail("test@example.com");
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //when
        User activeUser = underTest.getActiveUser();

        //then
        Assertions.assertThat(activeUser.getUsername()).isEqualTo("test@example.com");
    }

    /**
     * Test to ensure that the SecurityContext is populated on a successful login
     * and that a JWT is generated and returned in the response.
     */
    @Test
    public void signIn_populatesSecurityContextAndReturnsJwt_whenCredentialsAreValid() throws FailedAuthenticationException, AccountLockedException {
        // given a valid user with valid authentication and roles
        User user = new User();
        user.setEmail("test@example.com");
        Role role = new Role();
        role.setRoleName("admin");
        user.setRole(role);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        when(authenticationManager.authenticate(ArgumentMatchers.any())).thenReturn(authentication);

        // and a jwt will be generated
        when(jwtService.generateJwtToken(ArgumentMatchers.any())).thenReturn("jwt");

        // when
        SignInResponse response = underTest.signIn(new SignInRequest());

        // then
        verify(jwtService, Mockito.times(1)).generateJwtToken(user);
        Assertions.assertThat(response.getToken()).isEqualTo("jwt");
        Assertions.assertThat(response.getRoles()).containsExactly(role);
    }

    @Test
    public void passwordReset_UpdatesPassword_WhenUserExists() throws UserNotFoundException {
        // Arrange
        String email = "user@example.com";
        String newPassword = "newValidPassword";
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setEmail(email);
        passwordResetDTO.setPassword(newPassword);

        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setPassword(new BCryptPasswordEncoder().encode("oldPassword")); // Existing password

        // Set up mocks
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User result = underTest.passwordReset(passwordResetDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(new BCryptPasswordEncoder().matches(newPassword, result.getPassword())).isTrue();
        assertThat(result.getPassword()).isNotEqualTo(new BCryptPasswordEncoder().encode("oldPassword"));

        // Verify interactions
        verify(userService).getUserByEmail(email);
        verify(userRepository).save(existingUser);
    }

    @Test
    public void passwordReset_ThrowsUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";
        String newPassword = "newPassword";
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setEmail(email);
        passwordResetDTO.setPassword(newPassword);

        // Set up mocks
        when(userService.getUserByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException thrown = assertThrows(UserNotFoundException.class, () -> underTest.passwordReset(passwordResetDTO));

        assertThat(thrown.getMessage()).isEqualTo("User does not exist");

        // Verify interactions
        verify(userService).getUserByEmail(email);
    }

}

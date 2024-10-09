package com.calypso.binar.controller;

import com.calypso.binar.SignInRequest;
import com.calypso.binar.SignInResponse;
import com.calypso.binar.SignOutRequest;
import com.calypso.binar.model.Role;
import com.calypso.binar.model.dto.PasswordResetDTO;
import com.calypso.binar.service.AuthenticationService;
import com.calypso.binar.service.exception.AccountLockedException;
import com.calypso.binar.service.exception.FailedAuthenticationException;
import com.calypso.binar.service.exception.ServiceException;
import com.calypso.binar.service.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void signIn_returnsSignInResponseWithStatusOk_whenEmailAndPasswordAreValid() throws AccountLockedException, FailedAuthenticationException {
        // Arrange
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("user@example.com");
        signInRequest.setPassword("validPassword");

        Role role = new Role();
        role.setRoleName("Colleague");
        role.setRoleId(2);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        SignInResponse signInResponse = new SignInResponse("validToken", roles, false);

        when(authenticationService.signIn(signInRequest)).thenReturn(signInResponse);

        // Act
        ResponseEntity<SignInResponse> response = authenticationController.signIn(signInRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(signInResponse, response.getBody());
    }

    @Test
    public void signIn_throwsFailedAuthenticationException_whenEmailOrPasswordIsInvalid() throws AccountLockedException, FailedAuthenticationException {
        // Arrange
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("user@example.com");
        signInRequest.setPassword("invalidPassword");

        when(authenticationService.signIn(signInRequest)).thenThrow(new FailedAuthenticationException());

        // Act & Assert
        FailedAuthenticationException thrown = assertThrows(
                FailedAuthenticationException.class,
                () -> authenticationController.signIn(signInRequest)
        );

        assertThat(thrown.getMessage()).isEqualTo("Authentication failed, username or password wrong.");
    }

    @Test
    public void signIn_throwsAccountLockedException_whenAccountIsLocked() throws AccountLockedException, FailedAuthenticationException {
        // Arrange
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("user@example.com");
        signInRequest.setPassword("validPassword");

        when(authenticationService.signIn(signInRequest)).thenThrow(new AccountLockedException());

        // Act & Assert
        AccountLockedException thrown = assertThrows(
                AccountLockedException.class,
                () -> authenticationController.signIn(signInRequest)
        );

        assertThat(thrown.getMessage()).isEqualTo("Account is locked.");
    }

    @Test
    public void signOut_returnsSuccessMessage_whenSignOutIsSuccessful() {
        // Arrange
        SignOutRequest signOutRequest = new SignOutRequest();
        signOutRequest.setToken("validToken");

        doNothing().when(authenticationService).logOut(signOutRequest.getToken());

        // Act
        ResponseEntity<String> response = authenticationController.signOut(signOutRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isEqualTo("{\"response\": \"User signed out.\"}");

        // Verify that the logOut method was called with the correct token
        verify(authenticationService).logOut(signOutRequest.getToken());
    }

    @Test
    public void handleAuthExceptions_returnsErrorResponse_whenServiceExceptionIsThrown() {
        // Arrange
        ServiceException serviceException = new ServiceException("Service error", "SERVICE_ERROR_CODE");

        // Act
        ResponseEntity<Map<String, String>> response = authenticationController.handleAuthExceptions(serviceException);

        // Assert
        assertEquals(HttpStatus.valueOf(418), response.getStatusCode());
        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("error_code", "SERVICE_ERROR_CODE");
        assertEquals(expectedBody, response.getBody());
    }

    @Test
    public void changePassword_ReturnsSuccessMessage_WhenPasswordIsSuccessfullyChanged() throws UserNotFoundException {
        // Arrange
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setEmail("user@example.com");
        passwordResetDTO.setPassword("newValidPassword");

        // Act
        ResponseEntity<String> response = authenticationController.changePassword(passwordResetDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isEqualTo("{\"response\": \"Password changed successfully.\"}");

        // Verify that the passwordReset method was called with the correct password reset DTO
        verify(authenticationService).passwordReset(passwordResetDTO);
    }


    @Test
    public void changePassword_ThrowsUserNotFoundException_WhenEmailIsInvalid() throws UserNotFoundException {
        // Arrange
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setEmail("invaliduser@example.com");
        passwordResetDTO.setPassword("newValidPassword");

        doThrow(new UserNotFoundException()).when(authenticationService).passwordReset(passwordResetDTO);

        // Act & Assert
        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> authenticationController.changePassword(passwordResetDTO)
        );

        assertThat(thrown.getMessage()).isEqualTo("User does not exist");
    }



}

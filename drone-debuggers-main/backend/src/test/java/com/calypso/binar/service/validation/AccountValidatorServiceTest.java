package com.calypso.binar.service.validation;

import com.calypso.binar.model.User;
import com.calypso.binar.repository.UserRepository;
import com.calypso.binar.service.exception.AccountLockedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountValidatorServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountValidatorService accountValidatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isAccountLocked_accountLocked_shouldThrowException() {
        String email = "test@example.com";
        User user = new User();
        user.setAccountBlocked(true);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> accountValidatorService.isAccountLocked(email))
                .isInstanceOf(AccountLockedException.class);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void isAccountLocked_accountNotLocked_shouldNotThrowException() throws AccountLockedException {
        String email = "test@example.com";
        User user = new User();
        user.setAccountBlocked(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        accountValidatorService.isAccountLocked(email);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void failedLoginAttempt_userNotFound_shouldDoNothing() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        accountValidatorService.failedLoginAttempt(email);

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void failedLoginAttempt_userFound_shouldProcessAttempt() {
        String email = "test@example.com";
        User user = new User();
        user.setNumberOfFailedAttempts(3);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        accountValidatorService.failedLoginAttempt(email);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assert(savedUser.getNumberOfFailedAttempts() == 4);
    }

    @Test
    void failedLoginAttempt_reachesMaxFailedAttempts_shouldLockAccount() {
        String email = "test@example.com";
        User user = new User();
        user.setNumberOfFailedAttempts(5);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        accountValidatorService.failedLoginAttempt(email);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assert(savedUser.isAccountBlocked());
    }

    @Test
    void onLoginSuccessResetFailCount_shouldResetFailedAttempts() {
        String email = "test@example.com";
        User user = new User();
        user.setNumberOfFailedAttempts(3);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        accountValidatorService.onLoginSuccessResetFailCount(email);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assert(savedUser.getNumberOfFailedAttempts() == 0);
    }

    @Test
    void onLoginSuccessResetFailCount_noUserFound_shouldDoNothing() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        accountValidatorService.onLoginSuccessResetFailCount(email);

        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }
}

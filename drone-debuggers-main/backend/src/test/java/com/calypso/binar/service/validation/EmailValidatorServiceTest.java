package com.calypso.binar.service.validation;

import com.calypso.binar.model.User;
import com.calypso.binar.repository.UserRepository;
import com.calypso.binar.service.exception.InvalidEmailException;
import com.calypso.binar.service.exception.NonUniqueEmailException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmailValidatorServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailValidatorService underTest;

    @Test
    public void whenEmailIsValidAndUnique_NoExceptionIsThrown() {

        // Given a valid and unique email address
        String validEmailAddress = "valid.email@example.com";
        Mockito.when(userRepository.findByEmail(validEmailAddress)).thenReturn(Optional.empty());

        // When we validate the email
        Assertions.assertDoesNotThrow(() -> underTest.completeEmailValidation(validEmailAddress));
    }

    @Test
    public void whenEmailIsInvalid_InvalidEmailExceptionIsThrown() {

        // Given an invalid email address
        String invalidEmailAddress = "myemail. com";
        Mockito.when(userRepository.findByEmail(invalidEmailAddress)).thenReturn(Optional.empty());

        // When we validate the email
        InvalidEmailException exception = Assertions.assertThrows(InvalidEmailException.class,
                () -> underTest.completeEmailValidation(invalidEmailAddress));

        // Then the exception message should be as expected
        Assertions.assertEquals(invalidEmailAddress + " is not a valid email address!", exception.getMessage());
    }

    @Test
    public void whenEmailIsNotUnique_NonUniqeEmailExceptionIsThrown() {

        // Given an email that is not unique
        String nonUniqueEmail = "existing.email@example.com";
        Mockito.when(userRepository.findByEmail(nonUniqueEmail)).thenReturn(Optional.of(new User()));

        // When we validate the email
        NonUniqueEmailException exception = Assertions.assertThrows(NonUniqueEmailException.class,
                () -> underTest.completeEmailValidation(nonUniqueEmail));

        // Then the exception message should be as expected
        Assertions.assertEquals(nonUniqueEmail + " is already in use!", exception.getMessage());
    }

}

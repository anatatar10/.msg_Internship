package com.calypso.binar.service.validation;

import com.calypso.binar.repository.UserRepository;
import com.calypso.binar.service.exception.InvalidEmailException;
import com.calypso.binar.service.exception.NonUniqueEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidatorService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Autowired
    private UserRepository userRepository;

    private void validateEmailAddress(String email) throws InvalidEmailException {

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher emailMatcher = pattern.matcher(email);
        if(!emailMatcher.matches()) {
            throw new InvalidEmailException(email);
        }

    }

    private void validateEmailUniqueness(String email) throws NonUniqueEmailException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new NonUniqueEmailException(email);
        }
    }

    public void completeEmailValidation(String email) throws NonUniqueEmailException, InvalidEmailException {
        validateEmailUniqueness(email);
        validateEmailAddress(email);
    }

}

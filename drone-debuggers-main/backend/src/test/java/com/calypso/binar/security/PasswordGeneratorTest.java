package com.calypso.binar.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class PasswordGeneratorTest {

    @Test
    void generateRandomPassword_hasSameLengthAsGiven_whenNewlyGenerated() {
        PasswordGenerator generator = new PasswordGenerator();
        String randomPassword = generator.generateRandomPassword(12);
        Assertions.assertEquals(randomPassword.length(), 12);
    }

    @Test
    void generateRandomPassword_doesNotGenerateEqualPasswords_whenNewlyGenerated(){
        PasswordGenerator generator = new PasswordGenerator();
        String firstRandomPassword = generator.generateRandomPassword(12);
        String secondRandomPassword = generator.generateRandomPassword(12);
        Assertions.assertNotEquals(firstRandomPassword, secondRandomPassword);
    }

    @Test
    void generateRandomPassword_generatesPasswordWithDefaultLength() {
        PasswordGenerator generator = new PasswordGenerator();
        String randomPassword = generator.generateRandomPassword();
        Assertions.assertEquals(8, randomPassword.length());
    }

}
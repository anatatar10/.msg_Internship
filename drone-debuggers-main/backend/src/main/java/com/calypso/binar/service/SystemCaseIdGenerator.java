package com.calypso.binar.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class SystemCaseIdGenerator {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final SecureRandom random = new SecureRandom();

    public String generateSystemCaseId() {


        // Generate 3 random letters
        StringBuilder letters = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            letters.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        // Generate 3 random digits
        int digits = random.nextInt(900) + 100; // Ensures a 3-digit number

        // Combine letters and digits
        return letters.toString() + digits;
    }
}

package com.calypso.binar.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SystemCaseIdGeneratorTest {

    private SystemCaseIdGenerator systemCaseIdGenerator;

    @BeforeEach
    void setUp() {
        systemCaseIdGenerator = new SystemCaseIdGenerator();
    }

    @Test
    void generateSystemCaseId_formatIsCorrect() {
        String caseId = systemCaseIdGenerator.generateSystemCaseId();

        // Check that the ID is 6 characters long
        assertThat(caseId).hasSize(6);

        // Check that the first 3 characters are letters
        assertThat(caseId.substring(0, 3)).matches("[A-Z]{3}");

        // Check that the last 3 characters are digits
        assertThat(caseId.substring(3)).matches("\\d{3}");
    }

    @Test
    void generateSystemCaseId_isUnique() {
        Set<String> generatedIds = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            generatedIds.add(systemCaseIdGenerator.generateSystemCaseId());
        }

        // Check that all generated IDs are unique
        assertThat(generatedIds).hasSize(100);
    }
}

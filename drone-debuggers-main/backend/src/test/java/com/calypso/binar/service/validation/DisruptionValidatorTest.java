package com.calypso.binar.service.validation;

import com.calypso.binar.model.dto.CancellationTypeDTO;
import com.calypso.binar.model.dto.DisruptionDTO;
import com.calypso.binar.model.dto.DisruptionOptionDTO;
import com.calypso.binar.service.exception.CaseInvalidException;
import com.calypso.binar.service.exception.DescriptionTooLongException;
import com.calypso.binar.service.validation.casevalidation.DisruptionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class DisruptionValidatorTest {

    private DisruptionValidator disruptionValidator;

    @BeforeEach
    void setUp() {
        disruptionValidator = new DisruptionValidator();
    }

    @Test
    void validateIncidentDescription_validDescription_shouldPass() throws DescriptionTooLongException {
        DisruptionDTO disruption = createMockDisruption("This is a valid description", "Cancellation", "<14 days");

        disruptionValidator.validateIncidentDescription(disruption);
    }

    @Test
    void validateIncidentDescription_tooLongDescription_shouldThrowException() {
        String longDescription = "A".repeat(1025); // 1025 characters, which is too long
        DisruptionDTO disruption = createMockDisruption(longDescription, "Cancellation", "<14 days");

        assertThatThrownBy(() -> disruptionValidator.validateIncidentDescription(disruption))
                .isInstanceOf(DescriptionTooLongException.class);
    }


    @Test
    void validateEligibility_validCancellationOption_shouldPass() throws CaseInvalidException {
        DisruptionDTO disruption = createMockDisruption("Valid description", "Cancellation", "<14 days");

        disruptionValidator.validateEligibility(disruption);
    }

    @Test
    void validateEligibility_deniedBoardingOptionPresent_shouldPass() throws CaseInvalidException {
        DisruptionDTO disruption = createMockDisruption("Valid description", "Denied Boarding", "Any Option");

        disruptionValidator.validateEligibility(disruption);
    }

    @Test
    void validateEligibility_validDelayOption_shouldPass() throws CaseInvalidException {
        DisruptionDTO disruption = createMockDisruption("Valid description", "Delay", ">3 hours");

        disruptionValidator.validateEligibility(disruption);
    }

    @Test
    void validateEligibility_validCancellationLessThan14Days_shouldPass() throws CaseInvalidException {
        DisruptionDTO disruption = createMockDisruption("Valid description", "Cancellation", "<14 days");

        disruptionValidator.validateEligibility(disruption);
    }

    @Test
    void validateEligibility_invalidCancellation_shouldThrowException() {
        DisruptionDTO disruption = createMockDisruption("Valid description", "Cancellation", "Invalid Option");

        assertThatThrownBy(() -> disruptionValidator.validateEligibility(disruption))
                .isInstanceOf(CaseInvalidException.class)
                .hasMessage("Case is invalid");
    }

    @Test
    void validateEligibility_validDelayMoreThan3Hours_shouldPass() throws CaseInvalidException {
        DisruptionDTO disruption = createMockDisruption("Valid description", "Delay", ">3 hours");

        disruptionValidator.validateEligibility(disruption);
    }

    @Test
    void validateEligibility_invalidDelay_shouldThrowException() {
        DisruptionDTO disruption = createMockDisruption("Valid description", "Delay", "Invalid Option");

        assertThatThrownBy(() -> disruptionValidator.validateEligibility(disruption))
                .isInstanceOf(CaseInvalidException.class)
                .hasMessage("Case is invalid");
    }

    @Test
    void validateEligibility_validDeniedBoarding_shouldPass() throws CaseInvalidException {
        DisruptionDTO disruption = createMockDisruption("Valid description", "Denied Boarding", "Any Option");

        disruptionValidator.validateEligibility(disruption);
    }

    @Test
    void testValidateEligibility_invalidDisruptionType_shouldThrowException() {
        DisruptionDTO disruption = createMockDisruption("Valid description", "Invalid Type", "Invalid Option");

        assertThatThrownBy(() -> disruptionValidator.validateEligibility(disruption))
                .isInstanceOf(CaseInvalidException.class)
                .hasMessage("Case is invalid");
    }



    private DisruptionDTO createMockDisruption(String description, String disruptionType, String disruptionOption) {
        // Create actual instances of DisruptionDTO, CancellationTypeDTO, and DisruptionOptionDTO
        DisruptionDTO disruption = new DisruptionDTO();
        CancellationTypeDTO cancellationType = new CancellationTypeDTO();
        DisruptionOptionDTO disruptionOptionDTO = new DisruptionOptionDTO();

        // Set the properties of each object
        disruption.setIncidentDescription(description);
        cancellationType.setCancellationTypeDescription(disruptionType);
        disruption.setCancellationType(cancellationType);
        disruptionOptionDTO.setDisruptionOptionDescription(disruptionOption);
        disruption.setDisruptionOption(disruptionOptionDTO);

        return disruption;
    }
}

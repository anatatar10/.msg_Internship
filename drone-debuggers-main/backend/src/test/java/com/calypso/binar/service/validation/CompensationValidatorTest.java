package com.calypso.binar.service.validation;

import com.calypso.binar.service.compensation.CompensationService;
import com.calypso.binar.service.exception.CaseValidationException;
import com.calypso.binar.service.exception.CompensationServiceException;
import com.calypso.binar.service.exception.DistanceCalculationException;
import com.calypso.binar.service.validation.casevalidation.CompensationValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompensationValidatorTest {

    @Mock
    private CompensationService compensationService;

    @InjectMocks
    private CompensationValidator compensationValidator;

    @Test
    public void validateCompensation_shouldNotThrowException_whenCompensationIsCorrect() throws CompensationServiceException, DistanceCalculationException, CaseValidationException {
        // Arrange
        String departingAirport = "JFK";
        String destinationAirport = "LAX";
        int providedCompensation = 300;
        double calculatedCompensation = 300.0;

        when(compensationService.calculateCompensation(departingAirport, destinationAirport)).thenReturn(calculatedCompensation);

        // Act & Assert
        compensationValidator.validateCompensation(providedCompensation, departingAirport, destinationAirport);

        // Verify that the calculateCompensation method was called once
        verify(compensationService, times(1)).calculateCompensation(departingAirport, destinationAirport);
    }

    @Test
    public void validateCompensation_shouldThrowCaseValidationException_whenCompensationIsIncorrect() throws CompensationServiceException, DistanceCalculationException {
        // Arrange
        String departingAirport = "JFK";
        String destinationAirport = "LAX";
        int providedCompensation = 200;
        double calculatedCompensation = 300.0;

        when(compensationService.calculateCompensation(departingAirport, destinationAirport)).thenReturn(calculatedCompensation);

        // Act & Assert
        assertThrows(CaseValidationException.class, () -> {
            compensationValidator.validateCompensation(providedCompensation, departingAirport, destinationAirport);
        });

        // Verify that the calculateCompensation method was called once
        verify(compensationService, times(1)).calculateCompensation(departingAirport, destinationAirport);
    }

    @Test
    public void validateCompensation_shouldThrowCompensationServiceException_whenCompensationServiceFails() throws CompensationServiceException, DistanceCalculationException {
        // Arrange
        String departingAirport = "JFK";
        String destinationAirport = "LAX";
        int providedCompensation = 200;

        when(compensationService.calculateCompensation(departingAirport, destinationAirport)).thenThrow(new CompensationServiceException());

        // Act & Assert
        assertThrows(CompensationServiceException.class, () -> {
            compensationValidator.validateCompensation(providedCompensation, departingAirport, destinationAirport);
        });

        // Verify that the calculateCompensation method was called once
        verify(compensationService, times(1)).calculateCompensation(departingAirport, destinationAirport);
    }

    @Test
    public void validateCompensation_shouldThrowDistanceCalculationException_whenDistanceCalculationFails() throws CompensationServiceException, DistanceCalculationException {
        // Arrange
        String departingAirport = "JFK";
        String destinationAirport = "LAX";
        int providedCompensation = 200;

        when(compensationService.calculateCompensation(departingAirport, destinationAirport)).thenThrow(new DistanceCalculationException());

        // Act & Assert
        assertThrows(DistanceCalculationException.class, () -> {
            compensationValidator.validateCompensation(providedCompensation, departingAirport, destinationAirport);
        });

        // Verify that the calculateCompensation method was called once
        verify(compensationService, times(1)).calculateCompensation(departingAirport, destinationAirport);
    }
}

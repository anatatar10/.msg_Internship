package com.calypso.binar.service.validation;

import com.calypso.binar.model.dto.AttachedFilesDTO;
import com.calypso.binar.model.dto.CaseDTO;
import com.calypso.binar.service.exception.*;
import com.calypso.binar.service.validation.casevalidation.DisruptionValidator;
import com.calypso.binar.service.validation.casevalidation.FileValidator;
import com.calypso.binar.service.validation.casevalidation.FlightValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseValidatorTest {

    @Mock
    private DisruptionValidator disruptionValidator;

    @Mock
    private FlightValidator flightValidator;

    @Mock
    private FileValidator fileValidator;

    @InjectMocks
    private CaseValidator caseValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateCase_shouldValidateAllComponents() throws Exception {
        CaseDTO caseDTO = mock(CaseDTO.class);

        // Setup mock behavior for validation methods
        doNothing().when(disruptionValidator).validateIncidentDescription(any());
        doNothing().when(disruptionValidator).validateEligibility(any());
        doNothing().when(flightValidator).uniqueAirportsValidator(any(), any());
        doNothing().when(flightValidator).validateFlightDates(any());
        doNothing().when(flightValidator).validateConnectingFlights(any());
        doNothing().when(flightValidator).validateUniqueFlightNumbers(any());
        doNothing().when(flightValidator).validateFlightSequence(any());

        // Call the method under test
        caseValidator.validateCase(caseDTO);

        // Verify that each validator method is called exactly once
        verify(disruptionValidator, times(1)).validateIncidentDescription(any());
        verify(disruptionValidator, times(1)).validateEligibility(any());
        verify(flightValidator, times(1)).uniqueAirportsValidator(any(), any());
        verify(flightValidator, times(1)).validateFlightDates(any());
        verify(flightValidator, times(1)).validateConnectingFlights(any());
        verify(flightValidator, times(1)).validateUniqueFlightNumbers(any());
        verify(flightValidator, times(1)).validateFlightSequence(any());
    }

    @Test
    void validateFiles_shouldValidateEachFile() throws FileTooLargeException {
        AttachedFilesDTO file1 = mock(AttachedFilesDTO.class);
        AttachedFilesDTO file2 = mock(AttachedFilesDTO.class);
        AttachedFilesDTO[] files = new AttachedFilesDTO[]{file1, file2};

        // Setup mock behavior for file validator
        doNothing().when(fileValidator).isFileSizeValid(any());

        // Call the method under test
        caseValidator.validateFiles(files);

        // Verify that each file is validated
        verify(fileValidator, times(1)).isFileSizeValid(file1);
        verify(fileValidator, times(1)).isFileSizeValid(file2);
    }

    @Test
    void validateCase_shouldThrowExceptionForInvalidCase() throws Exception {
        CaseDTO caseDTO = mock(CaseDTO.class);

        // Setup mock behavior to throw an exception
        doThrow(new DescriptionTooLongException()).when(disruptionValidator).validateIncidentDescription(any());

        // Call the method under test and expect an exception
        assertThrows(DescriptionTooLongException.class, () -> caseValidator.validateCase(caseDTO));

        // Verify that the other methods are not called after the exception
        verify(disruptionValidator, times(1)).validateIncidentDescription(any());
        verify(disruptionValidator, never()).validateEligibility(any());
        verify(flightValidator, never()).uniqueAirportsValidator(any(), any());
    }

    @Test
    void validateFiles_shouldThrowExceptionForLargeFile() throws FileTooLargeException {
        AttachedFilesDTO file1 = mock(AttachedFilesDTO.class);
        AttachedFilesDTO[] files = new AttachedFilesDTO[]{file1};

        // Setup mock behavior to throw an exception
        doThrow(new FileTooLargeException()).when(fileValidator).isFileSizeValid(file1);

        // Call the method under test and expect an exception
        assertThrows(FileTooLargeException.class, () -> caseValidator.validateFiles(files));

        // Verify that the exception stops further validation
        verify(fileValidator, times(1)).isFileSizeValid(file1);
    }
}

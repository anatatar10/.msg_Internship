package com.calypso.binar.service;

import com.calypso.binar.model.*;
import com.calypso.binar.repository.CaseRepository;
import com.calypso.binar.repository.GeneratedPdfRepository;
import com.calypso.binar.service.exception.CaseInvalidException;
import com.calypso.binar.service.generate_pdf.PdfGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PdfGeneratorServiceTest {

    @Spy
    @InjectMocks
    private PdfGeneratorService pdfGeneratorService;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private GeneratedPdfRepository generatedPdfRepository;

    private Case caseEntity;

    public Case createTestCase(int caseId, String firstName, String lastName, String address, int passengerDetailsId, String reservationNumber, String status) {
        PassengerDetails passengerDetails = new PassengerDetails();
        passengerDetails.setAddress(address);
        passengerDetails.setPassengerDetailsId(passengerDetailsId);

        Reservation reservation = new Reservation();
        reservation.setReservationNumber(reservationNumber);

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);

        Case caseEntity = new Case();
        caseEntity.setCaseId(caseId);
        caseEntity.setStatus(new Status(status));
        caseEntity.setDateCreated(Timestamp.from(Instant.now()));
        caseEntity.setPassengerDetails(passengerDetails);
        caseEntity.setReservation(reservation);
        caseEntity.setPassenger(user);

        return caseEntity;
    }

    @BeforeEach
    void setUp() {
        caseEntity = createTestCase(1, "John", "Doe", "123 Main St", 1, "ABC123", "VALID");

    }

    @Test
    void generatePdf_validCase_returnsPdfBytes() throws IOException, CaseInvalidException {
        // Arrange
        when(caseRepository.findById(1)).thenReturn(Optional.of(caseEntity));

        // Act
        byte[] pdfBytes = pdfGeneratorService.generatePdf(1).getPdfData();

        // Assert
        assertThat(pdfBytes, is(notNullValue()));
        assertThat(pdfBytes.length, is(greaterThan(0)));
    }

    @Test
    void generatePdf_caseNotFound_throwsIllegalArgumentException() {
        // Arrange
        when(caseRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pdfGeneratorService.generatePdf(1));

        assertEquals("Case not found with ID: 1", exception.getMessage());
        verify(generatedPdfRepository, never()).save(any(GeneratedPdf.class));
    }

    @Test
    void generatePdf_invalidCase_throwsIllegalArgumentException() {
        // Arrange
        caseEntity.setStatus(new Status("INVALID"));
        when(caseRepository.findById(1)).thenReturn(Optional.of(caseEntity));

        // Act & Assert
        CaseInvalidException exception = assertThrows(CaseInvalidException.class, () -> pdfGeneratorService.generatePdf(1));

        assertEquals("Case is invalid", exception.getMessage());
        verify(generatedPdfRepository, never()).save(any(GeneratedPdf.class));
    }

    @Test
    void generatePdf_nullInputStream_throwsIOException() {
        // Arrange
        when(caseRepository.findById(1)).thenReturn(Optional.of(caseEntity));

        // Mock the getTemplateAsStream method to return null
        when(pdfGeneratorService.getTemplateAsStream(anyString())).thenReturn(null);

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> pdfGeneratorService.generatePdf(1));

        assertEquals("Failed to load PDF template.", exception.getMessage());
        verify(generatedPdfRepository, never()).save(any());
    }

    @Test
    void generatePdf_throwsException_whenCaseIdIsNullOrZero() {
        // Test for null systemCaseId
        IllegalArgumentException exceptionNull = assertThrows(IllegalArgumentException.class, () -> {
            pdfGeneratorService.generatePdf(null);
        });
        assertEquals("Case ID cannot be null or zero", exceptionNull.getMessage());

        // Test for systemCaseId equal to 0
        IllegalArgumentException exceptionZero = assertThrows(IllegalArgumentException.class, () -> {
            pdfGeneratorService.generatePdf(0);
        });
        assertEquals("Case ID cannot be null or zero", exceptionZero.getMessage());
    }

    @Test
    void generatePdf_throwsException_whenCaseIdIsNegative() {
        // Arrange & Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pdfGeneratorService.generatePdf(-1);
        });

        // Assert
        assertEquals("Case ID cannot be negative", exception.getMessage());
    }



    @Test
    void getGeneratedPdf_returnsPdf_whenDocumentIdExists() {

        GeneratedPdf generatedPdf = new GeneratedPdf("Contract_John_Doe_1.pdf", caseEntity, new byte[]{1, 2, 3}, Timestamp.from(Instant.now()));
        when(generatedPdfRepository.findById(1)).thenReturn(Optional.of(generatedPdf));

        Optional<GeneratedPdf> result = pdfGeneratorService.getGeneratedPdf(1);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(generatedPdf));
    }

    @Test
    void getGeneratedPdf_returnsEmpty_whenDocumentIdDoesNotExist() {
        when(generatedPdfRepository.findById(1)).thenReturn(Optional.empty());

        Optional<GeneratedPdf> result = pdfGeneratorService.getGeneratedPdf(1);

        assertThat(result.isPresent(), is(false));
    }








}

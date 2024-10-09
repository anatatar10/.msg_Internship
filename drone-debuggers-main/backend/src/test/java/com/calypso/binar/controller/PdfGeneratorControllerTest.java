package com.calypso.binar.controller;
import com.calypso.binar.model.PdfGenerationResult;
import com.calypso.binar.service.exception.CaseInvalidException;
import com.calypso.binar.service.generate_pdf.PdfGeneratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PdfGeneratorControllerTest {

    @Mock
    private PdfGeneratorService pdfGeneratorService;

    @InjectMocks
    private PdfGeneratorController pdfGeneratorController;

    @Test
    void generatePdf_validCaseId_returnsPdf() throws Exception {
        // Arrange
        byte[] pdfData = new byte[]{1, 2, 3};
        String fileName = "case_1.pdf";
        PdfGenerationResult pdfGenerationResult = new PdfGenerationResult(pdfData, fileName);

        when(pdfGeneratorService.generatePdf(1)).thenReturn(pdfGenerationResult);

        // Act
        ResponseEntity<?> response = pdfGeneratorController.generatePdf(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getHeaders().getContentType().includes(MediaType.APPLICATION_PDF));
        assertEquals("form-data; name=\"attachment\"; filename=\"case_1.pdf\"", response.getHeaders().getContentDisposition().toString());
        verify(pdfGeneratorService).generatePdf(1);
    }


    @Test
    void generatePdf_caseIdIsNull_returnsBadRequest() throws IOException, CaseInvalidException {
        // Act
        Mockito.when(pdfGeneratorService.generatePdf(0)).thenThrow(new IllegalArgumentException("Invalid case ID"));
        ResponseEntity<?> response = pdfGeneratorController.generatePdf(0);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void generatePdf_caseNotFound_returnsNotFound() throws Exception {
        // Arrange
        when(pdfGeneratorService.generatePdf(-4)).thenThrow(new IllegalArgumentException("Case not found"));

        // Act
        ResponseEntity<?> response = pdfGeneratorController.generatePdf(0);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void generatePdf_serviceThrowsIllegalArgumentException_returnsBadRequest() throws Exception {
        // Arrange
        when(pdfGeneratorService.generatePdf(-4)).thenThrow(new IllegalArgumentException("Invalid case ID"));

        // Act
        ResponseEntity<?> response = pdfGeneratorController.generatePdf(-4);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void generatePdf_serviceThrowsException_returnsInternalServerError() throws Exception {
        // Arrange
        when(pdfGeneratorService.generatePdf(1)).thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<?> response = pdfGeneratorController.generatePdf(1);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Convert the response body (byte array) to a string for comparison
        String responseBody = new String((byte[]) response.getBody());

        assertEquals("Failed to generate PDF: Unexpected error", responseBody);
    }

    @Test
    void generatePDF_returnsBadRequest_whenCaseIsNotValid() throws Exception {
        // Given
        Integer caseId = 123;
        String expectedMessage = "Case is not valid";

        when(pdfGeneratorService.generatePdf(caseId)).thenThrow(new CaseInvalidException(expectedMessage));
        ResponseEntity<?> response = pdfGeneratorController.generatePdf(123);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

}

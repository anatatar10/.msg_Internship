package com.calypso.binar.controller;

import com.calypso.binar.model.GeneratedPdf;
import com.calypso.binar.model.PdfGenerationResult;
import com.calypso.binar.model.dto.PDFShowHistoryDTO;
import com.calypso.binar.service.exception.CaseInvalidException;
import com.calypso.binar.service.exception.ServiceException;
import com.calypso.binar.service.generate_pdf.PdfGeneratorService;
import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path={"/api/pdf","/api/pdf/"})
public class PdfGeneratorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGeneratorController.class);

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    /**
     * Generate a PDF document based on the provided case ID
     * @param systemCaseId The ID of the case for which to generate the PDF
     * @return The byte array representing the generated PDF
     * @throws DocumentException If an error occurs during PDF generation
     * @throws IOException If an error occurs during file I/O
     */
    @GetMapping("/{systemCaseId}/generate")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Integer systemCaseId) {
        LOGGER.info("Received request to generate PDF for case ID: {}", systemCaseId);
        try {
            PdfGenerationResult result = pdfGeneratorService.generatePdf(systemCaseId);
            String fileName = result.getFileName();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(result.getPdfData());

        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid input for case ID {}: {}", systemCaseId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(("Invalid input: " + e.getMessage()).getBytes());
        } catch (CaseInvalidException e) {
            LOGGER.error("Invalid case for case ID {}: {}", systemCaseId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(("Invalid case: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            LOGGER.error("Failed to generate PDF for case ID {}: {}", systemCaseId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Failed to generate PDF: " + e.getMessage()).getBytes());
        }
    }

    /**
     * This method handles exceptions thrown by the service layer.
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, String>> handleAuthExceptions(ServiceException e) {
        // Create the error response as a map
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error_code", e.getExceptionID());

        // Return the error response with the appropriate HTTP status
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(418));
    }

    @GetMapping("/pdf-generated/{id}")
    public ResponseEntity<Optional<GeneratedPdf>> getGeneratedPdf(@PathVariable Integer id) {
        LOGGER.info("Received request to get generated PDF with ID: {}", id);
        try {
            Optional<GeneratedPdf> generatedPdf = pdfGeneratorService.getGeneratedPdf(id);
            return ResponseEntity.ok(generatedPdf);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid input for generated PDF ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (Exception e) {
            LOGGER.error("Failed to get generated PDF with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<PDFShowHistoryDTO>> getAllPdfs() {
        List<PDFShowHistoryDTO> pdfs = pdfGeneratorService.getAllPdfs();

        return ResponseEntity.ok(pdfs);


    }

}

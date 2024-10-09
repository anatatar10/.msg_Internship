package com.calypso.binar.controller;

import com.calypso.binar.model.dto.DocumentCaseDTO;
import com.calypso.binar.service.DocumentService;
import com.calypso.binar.service.exception.DocumentsNotFoundException;
import com.calypso.binar.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = {"/api/document", "/api/document/"})
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<DocumentCaseDTO>> getDocumentsForCase(@PathVariable String caseId){
        List<DocumentCaseDTO> documentInfoDTOS = documentService.getAllDocumentsForCase(caseId);

        return ResponseEntity.ok(documentInfoDTOS);
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Integer documentId) {
        try {
            // Retrieve the document data from the service layer
            DocumentCaseDTO documentDTO = documentService.getDocumentById(documentId);

            // Set the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF); // or MediaType.APPLICATION_OCTET_STREAM for other file types
            headers.setContentDispositionFormData("attachment", documentDTO.getFileName());

            // Return the document data as a byte array
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(documentDTO.getFileData());

        } catch (DocumentsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Document not found: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Failed to download document: " + e.getMessage()).getBytes());
        }
    }


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, String>> handleAuthExceptions(ServiceException e) {
        // Create the error response as a map
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error_code", e.getExceptionID());

        // Return the error response with the appropriate HTTP status
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(418));
    }

}

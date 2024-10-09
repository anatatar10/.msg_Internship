package com.calypso.binar.controller;

import com.calypso.binar.model.dto.DocumentCaseDTO;
import com.calypso.binar.service.DocumentService;
import com.calypso.binar.service.exception.DocumentsNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getDocumentsForCase_returnsDocumentDTOList_whenDocumentsExist() throws DocumentsNotFoundException {
        // Given
        int caseId = 1;

        DocumentCaseDTO document1 = new DocumentCaseDTO();
        document1.setDocumentId(101);
        document1.setDocumentType("Passport");

        DocumentCaseDTO document2 = new DocumentCaseDTO();
        document2.setDocumentId(102);
        document2.setDocumentType("ID");

        List<DocumentCaseDTO> documentDTOList = Arrays.asList(document1, document2);

        // Mocking the service to return a list of documents
        when(documentService.getAllDocumentsForCase(String.valueOf(caseId))).thenReturn(documentDTOList);

        // When
        ResponseEntity<List<DocumentCaseDTO>> response = documentController.getDocumentsForCase(String.valueOf(caseId));

        // Then
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(2);
        assertThat(response.getBody().get(0).getDocumentType()).isEqualTo("Passport");
        assertThat(response.getBody().get(1).getDocumentType()).isEqualTo("ID");

        // Verify that the service method was called once with the expected caseId
        verify(documentService, times(1)).getAllDocumentsForCase(String.valueOf(caseId));
    }


    @Test
    void getDocumentsForCase_success() {
        String caseId = "CASE123";
        List<DocumentCaseDTO> documents = Arrays.asList(new DocumentCaseDTO(), new DocumentCaseDTO());
        when(documentService.getAllDocumentsForCase(caseId)).thenReturn(documents);

        ResponseEntity<List<DocumentCaseDTO>> response = documentController.getDocumentsForCase(caseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isEqualTo(documents);
        verify(documentService, times(1)).getAllDocumentsForCase(caseId);
    }


    @Test
    void downloadDocument_documentNotFound() throws Exception {
        Integer documentId = 1;
        when(documentService.getDocumentById(documentId)).thenThrow(new DocumentsNotFoundException());

        ResponseEntity<byte[]> response = documentController.downloadDocument(documentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(new String(Objects.requireNonNull(response.getBody()))).contains("Document not found");
        verify(documentService, times(1)).getDocumentById(documentId);
    }

    @Test
    void downloadDocument_internalServerError() throws Exception {
        Integer documentId = 1;
        when(documentService.getDocumentById(documentId)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<byte[]> response = documentController.downloadDocument(documentId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertThat(new String(Objects.requireNonNull(response.getBody()))).contains("Failed to download document");
        verify(documentService, times(1)).getDocumentById(documentId);
    }



}

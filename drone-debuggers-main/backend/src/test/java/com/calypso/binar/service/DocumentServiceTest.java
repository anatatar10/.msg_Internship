package com.calypso.binar.service;

import com.calypso.binar.model.Document;
import com.calypso.binar.model.DocumentType;
import com.calypso.binar.model.dto.AttachedFilesDTO;
import com.calypso.binar.model.dto.DocumentCaseDTO;
import com.calypso.binar.repository.CaseRepository;
import com.calypso.binar.repository.DocumentRepository;
import com.calypso.binar.repository.DocumentTypeRepository;
import com.calypso.binar.service.exception.DocumentsNotFoundException;
import com.calypso.binar.service.exception.InvalidFileType;
import com.calypso.binar.service.validation.CaseValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentTypeRepository documentTypeRepository;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private CaseValidator caseValidator;

    @InjectMocks
    private DocumentService documentService;

    private static final String PDF_FILE_TYPE = "application/pdf";
    private static final String JPEG_FILE_TYPE = "image/jpeg";
    private static final String JPG_FILE_TYPE = "image/jpg";
    private static final String PNG_FILE_TYPE = "image/png";

    @Test
    public void getAllDocumentsForCase_returnsDocuments_whenDocumentsExist() {
        // Given
        String caseId = "case123";

        // Create DocumentType object
        DocumentType documentType = new DocumentType();
        documentType.setDocumentTypeId(1);
        documentType.setDocumentTypeName("Passport");

        // Create fully populated Document objects
        Document document1 = new Document();
        document1.setDocumentId(101);
        document1.setDocumentType(documentType);
        document1.setData(new byte[]{1, 2, 3});

        Document document2 = new Document();
        document2.setDocumentId(102);
        document2.setDocumentType(documentType);
        document2.setData(new byte[]{4, 5, 6});

        // Create the list of documents
        List<Document> documents = Arrays.asList(document1, document2);

        // Mock the repository call
        when(documentRepository.findAllByCaseId(caseId)).thenReturn(Optional.of(documents));

        // When
        List<DocumentCaseDTO> result = documentService.getAllDocumentsForCase(caseId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getDocumentId()).isEqualTo(101);
        assertThat(result.get(1).getDocumentId()).isEqualTo(102);

        // Additional assertions on document type
        assertThat(result.get(0).getDocumentType()).isEqualTo("Passport");
        assertThat(result.get(1).getDocumentType()).isEqualTo("Passport");

        // Check data
        assertThat(result.get(0).getFileData()).isEqualTo(new byte[]{1, 2, 3});
        assertThat(result.get(1).getFileData()).isEqualTo(new byte[]{4, 5, 6});
    }

    @Test
    public void getAllDocumentsForCase_returnsEmptyList_whenNoDocumentsFound() {
        // Given
        String caseId = "case123";
        // Mocking the repository to return an empty Optional list
        when(documentRepository.findAllByCaseId(caseId)).thenReturn(Optional.of(Collections.emptyList()));

        // When
        List<DocumentCaseDTO> result = documentService.getAllDocumentsForCase(caseId);

        // Then
        assertEquals(0, result.size());
    }


    @Test
    public void saveCaseFiles_throwsInvalidFileType_whenInvalidFileTypeProvided() {
        // Given
        String systemCaseId = "CASE123";
        AttachedFilesDTO file = new AttachedFilesDTO();
        file.setFileType("application/unknown");
        file.setFileData(new byte[]{1, 2, 3});
        file.setDocumentType("Unknown");
        AttachedFilesDTO[] attachments = new AttachedFilesDTO[]{file};

        // Act & Assert
        assertThrows(InvalidFileType.class, () -> documentService.saveCaseFiles(systemCaseId, attachments));
    }


    @Test
    public void getDocumentById_returnsDocument_whenDocumentExists() throws DocumentsNotFoundException {
        // Given
        Integer documentId = 1;
        Document document = new Document();
        document.setDocumentId(documentId);
        DocumentType documentType = new DocumentType();
        documentType.setDocumentTypeName("Passport");
        document.setDocumentType(documentType);
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(document));

        // When
        DocumentCaseDTO result = documentService.getDocumentById(documentId);

        // Then
        assertNotNull(result);
        assertEquals(documentId, result.getDocumentId());
        assertEquals("Passport", result.getDocumentType());
        verify(documentRepository, times(1)).findById(documentId);
    }

    @Test
    public void getDocumentById_throwsDocumentsNotFoundException_whenDocumentDoesNotExist() {
        // Given
        Integer documentId = 1;
        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DocumentsNotFoundException.class, () -> documentService.getDocumentById(documentId));
    }
}

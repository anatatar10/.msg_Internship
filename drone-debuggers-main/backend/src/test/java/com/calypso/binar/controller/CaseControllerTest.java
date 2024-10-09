package com.calypso.binar.controller;

import com.calypso.binar.model.dto.*;
import com.calypso.binar.service.DocumentService;
import com.calypso.binar.service.EmailSenderService;
import com.calypso.binar.service.casedetails.CaseService;
import com.calypso.binar.service.exception.CaseNotFoundException;
import com.calypso.binar.service.exception.FileTooLargeException;
import com.calypso.binar.service.exception.InvalidFileType;
import com.calypso.binar.service.exception.ServiceException;
import com.calypso.binar.service.savecase.SaveCaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaseControllerTest {

    @Mock
    private CaseService caseService;

    @Mock
    private SaveCaseService saveCaseService;

    @Mock
    private DocumentService documentService;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private CaseController caseController;

    @Test
    public void getAllCaseSummaries_returnListOfCaseSummaryDTOs() {
        // Arrange
        List<CaseSummaryDTO> caseSummaries = Arrays.asList(new CaseSummaryDTO(), new CaseSummaryDTO());
        when(caseService.getAllCaseSummaries()).thenReturn(caseSummaries);

        // Act
        List<CaseSummaryDTO> result = caseController.getAllCaseSummaries();

        // Assert
        assertEquals(caseSummaries, result);
        verify(caseService, times(1)).getAllCaseSummaries();
    }

    @Test
    public void getCaseSummaryById_returnCaseDTO() {
        // Arrange
        int caseId = 1;
        CaseDTO caseDTO = new CaseDTO();
        when(caseService.getCaseSummaryById(caseId)).thenReturn(caseDTO);

        // Act
        CaseDTO result = caseController.getCaseSummaryById(caseId);

        // Assert
        assertEquals(caseDTO, result);
        verify(caseService, times(1)).getCaseSummaryById(caseId);
    }

    @Test
    public void getAllColleagues_returnListOfColleagueDTOs() {
        // Arrange
        List<ColleagueDTO> colleagues = Arrays.asList(new ColleagueDTO(), new ColleagueDTO());
        when(caseService.getAllColleagues()).thenReturn(colleagues);

        // Act
        List<ColleagueDTO> result = caseController.getAllColleagues();

        // Assert
        assertEquals(colleagues, result);
        verify(caseService, times(1)).getAllColleagues();
    }

    @Test
    public void assignColleague_shouldReturnOk() {
        // Arrange
        AssignCaseDTO assignCaseDTO = new AssignCaseDTO();
        assignCaseDTO.setSystemCaseId("case123");
        assignCaseDTO.setAssignedColleagueEmail("colleague@example.com");

        // Act
        ResponseEntity<Void> response = caseController.assignColleague(assignCaseDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(caseService, times(1)).assignColleague("case123", "colleague@example.com");
    }

    @Test
    public void updateCaseStatus_shouldReturnOk() {
        // Arrange
        String systemCaseId = "case123";
        Map<String, String> payload = new HashMap<>();
        payload.put("status", "NEW");
        doNothing().when(emailSenderService).sendUpdate(systemCaseId,"NEW");
        // Act
        ResponseEntity<Void> response = caseController.updateCaseStatus(systemCaseId, payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(caseService, times(1)).updateCaseStatus(systemCaseId, "NEW");
    }

    @Test
    public void createCase_shouldReturnOkWithCreatedCase() throws ServiceException {
        // Arrange
        CaseDTO caseDTO = new CaseDTO();
        String createdCase = "case123";
        when(saveCaseService.saveCase(caseDTO)).thenReturn(createdCase);

        // Act
        ResponseEntity<String> response = caseController.createCase(caseDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdCase, response.getBody());
        verify(saveCaseService, times(1)).saveCase(caseDTO);
    }

    @Test
    public void uploadCaseFiles_shouldReturnOk() throws IOException, InvalidFileType, FileTooLargeException {
        // Arrange
        String systemCaseId = "case123";
        AttachedFilesDTO[] attachments = new AttachedFilesDTO[]{new AttachedFilesDTO(), new AttachedFilesDTO()};

        // Act
        ResponseEntity response = caseController.uploadCaseFiles(systemCaseId, attachments);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(documentService, times(1)).saveCaseFiles(systemCaseId, attachments);
    }

    @Test
    public void uploadCaseFiles_shouldReturnOk_whenFilesAreUploadedSuccessfully() throws IOException, InvalidFileType, FileTooLargeException {
        // Arrange
        String systemCaseId = "case123";
        AttachedFilesDTO[] attachments = new AttachedFilesDTO[]{new AttachedFilesDTO(), new AttachedFilesDTO()};

        // Act
        ResponseEntity response = caseController.uploadCaseFiles(systemCaseId, attachments);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(documentService, times(1)).saveCaseFiles(systemCaseId, attachments);
    }

    @Test
    public void uploadCaseFiles_shouldThrowInvalidFileTypeException_whenFileTypeIsInvalid() throws IOException, InvalidFileType, FileTooLargeException {
        // Arrange
        String systemCaseId = "case123";
        AttachedFilesDTO[] attachments = new AttachedFilesDTO[]{new AttachedFilesDTO()};
        doThrow(new InvalidFileType("Invalid file type")).when(documentService).saveCaseFiles(systemCaseId, attachments);

        // Act & Assert
        assertThrows(InvalidFileType.class, () -> caseController.uploadCaseFiles(systemCaseId, attachments));
        verify(documentService, times(1)).saveCaseFiles(systemCaseId, attachments);
    }



    @Test
    public void sendPdf_shouldThrowCaseNotFoundException_whenCaseIsNotFound() throws CaseNotFoundException {
        // Arrange
        String systemCaseId = "case123";
        when(caseService.getCaseBySystemCaseId(systemCaseId)).thenThrow(new CaseNotFoundException("Case not found"));

        // Act & Assert
        assertThrows(CaseNotFoundException.class, () -> caseController.sendPdf(systemCaseId));
        verify(caseService, times(1)).getCaseBySystemCaseId(systemCaseId);
    }


    @Test
    public void handleCaseNotFoundException_shouldReturnNotFoundStatus() {
        // Act
        ResponseEntity<Void> response = caseController.handleCaseNotFoundException(new CaseNotFoundException("Case not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void handleAuthExceptions_shouldReturnBadRequestWithErrorCode() {
        ServiceException serviceException = new ServiceException("Invalid operation", "ERR123");

        ResponseEntity<Map<String, String>> response = caseController.handleAuthExceptions(serviceException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ERR123", Objects.requireNonNull(response.getBody()).get("error_code"));
    }




}

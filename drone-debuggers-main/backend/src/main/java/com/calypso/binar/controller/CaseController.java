package com.calypso.binar.controller;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.PdfGenerationResult;
import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.*;
import com.calypso.binar.service.DocumentService;
import com.calypso.binar.service.EmailSenderService;
import com.calypso.binar.service.UserService;
import com.calypso.binar.service.casedetails.CaseService;
import com.calypso.binar.service.exception.*;
import com.calypso.binar.service.generate_pdf.PdfGeneratorService;
import com.calypso.binar.service.savecase.SaveCaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = {"/api/case", "/api/case/"})
public class CaseController {

    @Autowired
    private CaseService caseService;

    @Autowired
    private SaveCaseService saveCaseService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserService userService;


    @GetMapping("/summaries")
    public List<CaseSummaryDTO> getAllCaseSummaries() {
        return caseService.getAllCaseSummaries();
    }

    @GetMapping("/{id}")
    public CaseDTO getCaseSummaryById(@PathVariable int id) {
        return caseService.getCaseSummaryById(id);
    }

    @GetMapping("/colleagues")
    public List<ColleagueDTO> getAllColleagues() {
        return caseService.getAllColleagues();
    }

    @PostMapping("/assign")
    @Secured({"Colleague"})
    public ResponseEntity<Void> assignColleague(@RequestBody AssignCaseDTO assignCaseDTO) {
        caseService.assignColleague(assignCaseDTO.getSystemCaseId(), assignCaseDTO.getAssignedColleagueEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{systemCaseId}/status")
    @Secured({"Colleague"})
    public ResponseEntity<Void> updateCaseStatus(@PathVariable String systemCaseId, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");
        caseService.updateCaseStatus(systemCaseId, newStatus);
        emailSenderService.sendUpdate(systemCaseId, newStatus);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/show-list")
    @Secured({"System Admin"})
    public ResponseEntity<List<CaseListDTO>> findAllCasesForAdmin() {
        List<CaseListDTO> cases = caseService.getAllCasesForAdmin();
        return ResponseEntity.ok(cases);
    }

    /**
     * Delete case by id
     */
    @DeleteMapping("/{id}")
    @Secured({"System Admin"})
    public ResponseEntity<Void> deleteCase(@PathVariable int id) throws CaseNotFoundException {
        caseService.deleteCaseById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/save")
    public ResponseEntity<String> createCase(@Valid @RequestBody CaseDTO caseDTO) throws ServiceException {
        String response = saveCaseService.saveCase(caseDTO);


        return ResponseEntity.ok(response);
    }


    @PostMapping(value="/uploadCaseFiles/{systemCaseId}")
    public ResponseEntity<String> uploadCaseFiles(@PathVariable String systemCaseId,@Valid @RequestBody AttachedFilesDTO[] attachments) throws IOException, InvalidFileType, FileTooLargeException {
        this.documentService.saveCaseFiles(systemCaseId, attachments);

        String jsonResponse="{\"response\":\"Upload Files successfully\"}";
        return ResponseEntity.ok(jsonResponse);
        }


    @GetMapping(value="/sendPdf/{systemCaseId}")
    public ResponseEntity<String> sendPdf(@PathVariable String systemCaseId) throws IOException, CaseNotFoundException, CaseInvalidException {
        Case compensationCase = this.caseService.getCaseBySystemCaseId(systemCaseId);
        PdfGenerationResult generatedPdf = this.pdfGeneratorService.generatePdf(compensationCase.getCaseId());

        this.emailSenderService.sendPdf(compensationCase.getPassenger().getEmail(), generatedPdf, compensationCase.getSystemCaseId());

        String jsonResponse="{\"response\":\"Email send successfully\"}";
        return ResponseEntity.ok(jsonResponse);
    }


    /**
     * Handles the HTTP GET request to retrieve a list of past cases for a passenger.
     * This method maps to the "/past-cases/{email}" endpoint.
     *
     * @param email the ID of the passenger whose past cases are being requested
     * @return a ResponseEntity containing a list of PastCaseDTO representing the past cases
     */
    @GetMapping("/past-cases/{email}")
    public ResponseEntity<List<PastCaseDTO>> getPastCaseSummaryPassenger(@PathVariable String email) throws StatusNonExistent {
        Optional<User> user= this.userService.getUserByEmail(email);

        List<PastCaseDTO> caseDTOs = caseService.getPastCasesPassenger(user.get().getUserId());

        return ResponseEntity.ok(caseDTOs);
    }

    /**
     * Handles the HTTP GET request to retrieve a list of active cases for a passenger.
     * This method maps to the "/active-cases/{email}" endpoint.
     *
     * @param email the ID of the passenger whose past cases are being requested
     * @return a ResponseEntity containing a list of PastCaseDTO representing the active cases
     */
    @GetMapping("/active-cases/{email}")
    public ResponseEntity<List<PastCaseDTO>> getActiveCaseSummaryPassenger(@PathVariable String email) throws StatusNonExistent {
        Optional<User> user= this.userService.getUserByEmail(email);

        List<PastCaseDTO> caseDTOs = caseService.getActiveCasesPassenger(user.get().getUserId());

        return ResponseEntity.ok(caseDTOs);
    }

    @ExceptionHandler(CaseNotFoundException.class)
    public ResponseEntity<Void> handleCaseNotFoundException(CaseNotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, String>> handleAuthExceptions(ServiceException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error_code", e.getExceptionID());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

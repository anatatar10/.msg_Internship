package com.calypso.binar.controller;

import com.calypso.binar.model.dto.PassengerDetailsDTO;
import com.calypso.binar.service.PassengerDetailsService;
import com.calypso.binar.service.exception.PassengerDetailsNotFoundException;
import com.calypso.binar.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path={"/api/passenger-details", "/api/passenger-details/"})
public class PassengerDetailsController {

    @Autowired
    private PassengerDetailsService passengerDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDetailsDTO> getPassengerDetailsByID(@PathVariable("id") int id) {
        PassengerDetailsDTO passengerDetailsDTO = passengerDetailsService.getPassengerDetailsById(id);
        return new ResponseEntity<>(passengerDetailsDTO, HttpStatus.OK);
    }

    @GetMapping("/case/{caseId}")
    public ResponseEntity<PassengerDetailsDTO> getPassengerDetailsForCase(@PathVariable("caseId") String caseId) throws PassengerDetailsNotFoundException {
        PassengerDetailsDTO passengerDetailsDTO = passengerDetailsService.getPassengerDetailsForCase(caseId);

        return ResponseEntity.ok(passengerDetailsDTO);
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

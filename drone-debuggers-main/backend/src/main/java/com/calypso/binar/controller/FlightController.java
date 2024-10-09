package com.calypso.binar.controller;

import com.calypso.binar.model.dto.FlightInfoDTO;
import com.calypso.binar.service.FlightService;
import com.calypso.binar.service.exception.FlightsNotFoundException;
import com.calypso.binar.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = {"/api/flight", "/api/flight/"})
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/case/{caseId}")
    public ResponseEntity<List<FlightInfoDTO>> getFlightsForCase(@PathVariable String caseId) throws FlightsNotFoundException {
        List<FlightInfoDTO> flightInfoDTOS = flightService.getAllFlightsDTOForCase(caseId);

        return ResponseEntity.ok(flightInfoDTOS);
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

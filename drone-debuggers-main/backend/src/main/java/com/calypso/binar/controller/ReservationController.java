package com.calypso.binar.controller;

import com.calypso.binar.model.dto.ReservationDTO;
import com.calypso.binar.service.ReservationService;
import com.calypso.binar.service.exception.ReservationNotFoundException;
import com.calypso.binar.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = {"/api/reservation", "/api/reservation/"})
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/case/{caseId}")
    public ResponseEntity<ReservationDTO> getReservationsForCase(@PathVariable String caseId) throws ReservationNotFoundException {
        ReservationDTO reservationDTO = reservationService.getReservationForCase(caseId);

        return ResponseEntity.ok(reservationDTO);
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


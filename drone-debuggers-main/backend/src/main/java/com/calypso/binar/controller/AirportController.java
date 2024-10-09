package com.calypso.binar.controller;

import com.calypso.binar.model.Airport;
import com.calypso.binar.service.airport.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = {"/api/airport", "/api/airport/"})
public class AirportController {

    @Autowired
    private AirportService airportService;

    @GetMapping("/all")
    public ResponseEntity<List<Airport>> getAllAirports() {
        List<Airport> airports = airportService.getAllAirports();
        if (airports == null) {
            airports = Collections.emptyList();
        }
        return ResponseEntity.ok(airports);
    }

}

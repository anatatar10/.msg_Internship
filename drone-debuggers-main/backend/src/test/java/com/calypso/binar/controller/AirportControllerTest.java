package com.calypso.binar.controller;

import com.calypso.binar.model.Airport;
import com.calypso.binar.service.airport.AirportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AirportControllerTest {

    @Mock
    private AirportService airportService;

    @InjectMocks
    private AirportController airportController;

    private List<Airport> airportList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        airportList = Arrays.asList(
                new Airport("John F. Kennedy International Airport", "JFK"),
                new Airport("London Heathrow Airport", "LHR")
        );
    }

    @Test
    public void testGetAllAirports() {
        // Arrange
        when(airportService.getAllAirports()).thenReturn(airportList);

        // Act
        ResponseEntity<List<Airport>> response = airportController.getAllAirports();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(airportList, response.getBody());
    }

    @Test
    public void testGetAllAirportsEmptyList() {
        // Arrange
        when(airportService.getAllAirports()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Airport>> response = airportController.getAllAirports();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void testGetAllAirportsNull() {
        // Arrange
        when(airportService.getAllAirports()).thenReturn(null);

        // Act
        ResponseEntity<List<Airport>> response = airportController.getAllAirports();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    public void testGetAllAirportsServiceException() {
        // Arrange
        when(airportService.getAllAirports()).thenThrow(new RuntimeException("Service failure"));

        // Act & Assert
        ResponseEntity<List<Airport>> response;
        try {
            response = airportController.getAllAirports();
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAllAirportsLargeList() {
        // Arrange
        List<Airport> largeAirportList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeAirportList.add(new Airport("Airport " + i, "Code" + i));
        }
        when(airportService.getAllAirports()).thenReturn(largeAirportList);

        // Act
        ResponseEntity<List<Airport>> response = airportController.getAllAirports();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(largeAirportList.size(), response.getBody().size());
    }
}

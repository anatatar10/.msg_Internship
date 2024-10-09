package com.calypso.binar.service;

import com.calypso.binar.model.Airport;
import com.calypso.binar.repository.AirportRepository;
import com.calypso.binar.service.airport.AirportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportService airportService;

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
    public void getAllAirports() {
        // Arrange
        when(airportRepository.findAllActive()).thenReturn(airportList);

        // Act
        List<Airport> result = airportService.getAllAirports();

        // Assert
        assertEquals(airportList, result);
    }

    @Test
    public void tgetAllAirportsEmptyList() {
        // Arrange
        when(airportRepository.findAllActive()).thenReturn(Collections.emptyList());

        // Act
        List<Airport> result = airportService.getAllAirports();

        // Assert
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void getAllAirportsException() {
        // Arrange
        when(airportRepository.findAllActive()).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        try {
            airportService.getAllAirports();
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Database connection failed", e.getMessage());
        }
    }
}

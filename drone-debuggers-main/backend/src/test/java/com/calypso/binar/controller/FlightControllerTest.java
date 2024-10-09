package com.calypso.binar.controller;

import com.calypso.binar.model.dto.FlightInfoDTO;
import com.calypso.binar.service.FlightService;
import com.calypso.binar.service.exception.FlightsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    @Test
    public void getFlightsForCase_returnsFlightDTOList_whenFlightsExist() throws FlightsNotFoundException {
        // Given
        int caseId = 1;

        FlightInfoDTO flight1 = new FlightInfoDTO();
        flight1.setFlightNumber("FL123");
        flight1.setAirline("Airline 1");

        FlightInfoDTO flight2 = new FlightInfoDTO();
        flight2.setFlightNumber("FL456");
        flight2.setAirline("Airline 2");

        List<FlightInfoDTO> flightInfoDTOList = Arrays.asList(flight1, flight2);

        // Mocking the service to return a list of flights
        when(flightService.getAllFlightsDTOForCase(String.valueOf(caseId))).thenReturn(flightInfoDTOList);

        // When
        ResponseEntity<List<FlightInfoDTO>> response = flightController.getFlightsForCase(String.valueOf(caseId));

        // Then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(0).getFlightNumber()).isEqualTo("FL123");
        assertThat(response.getBody().get(1).getFlightNumber()).isEqualTo("FL456");

        // Verify that the service method was called once with the expected caseId
        verify(flightService, Mockito.times(1)).getAllFlightsDTOForCase(String.valueOf(caseId));
    }

    @Test
    public void getFlightsForCase_throwsFlightsNotFoundException_whenNoFlightsExist() throws FlightsNotFoundException {
        // Given
        int caseId = 1;

        // Mocking the service to throw a FlightsNotFoundException
        when(flightService.getAllFlightsDTOForCase(String.valueOf(caseId))).thenThrow(new FlightsNotFoundException());

        // When & Then
        FlightsNotFoundException thrown = assertThrows(FlightsNotFoundException.class, () -> {
            flightController.getFlightsForCase(String.valueOf(caseId));
        });

        // Verify that the service method was called once with the expected caseId
        verify(flightService, Mockito.times(1)).getAllFlightsDTOForCase(String.valueOf(caseId));

        // Assert that the exception has the expected message
        assertThat(thrown).hasMessageContaining("Flights not found");
    }
}


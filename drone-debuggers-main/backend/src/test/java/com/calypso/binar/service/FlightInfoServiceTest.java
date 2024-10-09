package com.calypso.binar.service;

import com.calypso.binar.model.Airport;
import com.calypso.binar.model.FlightInfo;
import com.calypso.binar.model.dto.FlightInfoDTO;
import com.calypso.binar.repository.FlightInfoRepository;
import com.calypso.binar.service.exception.FlightsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightInfoServiceTest {

    @Mock
    private FlightInfoRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    public void getAllFlightsDTOForCase_returnsFlightInfoDTOs_whenFlightsExist() throws FlightsNotFoundException {
        // Given
        int caseId = 1;

        FlightInfo flight1 = new FlightInfo();
        flight1.setFlightInfoId(101);
        flight1.setAirline("Airline1");
        flight1.setPlannedDepartureDate(new Date());
        flight1.setPlannedArrivalDate(new Date());
        flight1.setFlightNr("AB123");
        flight1.setDepartingAirport(new Airport("L","123"));
        flight1.setDestinationAirport(new Airport("K","124"));
        flight1.getDestinationAirport().setAirportId(1);
        flight1.getDepartingAirport().setAirportId(1);
        flight1.setProblemFlight(false);

        FlightInfo flight2 = new FlightInfo();
        flight2.setFlightInfoId(102);
        flight2.setAirline("Airline2");
        flight2.setPlannedDepartureDate(new Date());
        flight2.setPlannedArrivalDate(new Date());
        flight2.setFlightNr("CD456");
        flight2.setDepartingAirport(new Airport("M","125"));
        flight2.getDepartingAirport().setAirportId(3);

        flight2.setDestinationAirport(new Airport("N","126"));
        flight2.getDestinationAirport().setAirportId(4);

        flight2.setProblemFlight(true);

        List<FlightInfo> flights = Arrays.asList(flight1, flight2);

        // Mock the repository call
        when(flightRepository.findAllByCaseId(String.valueOf(caseId))).thenReturn(Optional.of(flights));

        // When
        List<FlightInfoDTO> flightDTOs = flightService.getAllFlightsDTOForCase(String.valueOf(caseId));

        // Then
        assertThat(flightDTOs).isNotNull();
        assertThat(flightDTOs.size()).isEqualTo(2);
        assertThat(flightDTOs.get(0).getFlightNumber()).isEqualTo("AB123");
        assertThat(flightDTOs.get(0).getAirline()).isEqualTo("Airline1");
        assertThat(flightDTOs.get(1).getFlightNumber()).isEqualTo("CD456");
        assertThat(flightDTOs.get(1).getAirline()).isEqualTo("Airline2");
    }

    @Test
    public void getAllFlightsDTOForCase_throwsFlightsNotFoundException_whenNoFlightsExist() {
        // Given
        int caseId = 1;

        // Mock the repository to return an empty Optional
        when(flightRepository.findAllByCaseId(String.valueOf(caseId))).thenReturn(Optional.empty());

        // When & Then
        FlightsNotFoundException thrown = assertThrows(FlightsNotFoundException.class, () -> {
            flightService.getAllFlightsDTOForCase(String.valueOf(caseId));
        });
        assertThat(thrown).hasMessageContaining("Flights not found");
    }
}


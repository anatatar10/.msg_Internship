package com.calypso.binar.service.validation;

import com.calypso.binar.model.dto.AirportDTO;
import com.calypso.binar.model.dto.FlightInfoDTO;
import com.calypso.binar.model.dto.ReservationSaveCaseDTO;
import com.calypso.binar.service.exception.DuplicateAirportException;
import com.calypso.binar.service.exception.DuplicateFlightNumberException;
import com.calypso.binar.service.exception.FlightSequenceException;
import com.calypso.binar.service.exception.WrongFlightDatesException;
import com.calypso.binar.service.validation.casevalidation.FlightValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlightValidatorTest {

    private FlightValidator flightValidator;

    @BeforeEach
    void setUp() {
        flightValidator = new FlightValidator();
    }

    @Test
    void uniqueAirportsValidator_noDuplicateAirports_shouldPass() throws DuplicateAirportException {
        ReservationSaveCaseDTO reservation = createMockReservation("JFK", "LAX");
        List<FlightInfoDTO> flights = new ArrayList<>();
        flights.add(createMockFlight("JFK", "LAX"));
        flights.add(createMockFlight("LAX", "SFO"));

        flightValidator.uniqueAirportsValidator(reservation, flights);
    }

    @Test
    void uniqueAirportsValidator_duplicateAirports_shouldThrowException() {
        ReservationSaveCaseDTO reservation = createMockReservation("JFK", "LAX");
        List<FlightInfoDTO> flights = new ArrayList<>();
        flights.add(createMockFlight("JFK", "LAX"));
        flights.add(createMockFlight("LAX", "JFK"));

        assertThatExceptionOfType(DuplicateAirportException.class)
                .isThrownBy(() -> flightValidator.uniqueAirportsValidator(reservation, flights));
    }

    @Test
    void validateConnectingFlights_validSequence_shouldPass() throws FlightSequenceException {
        List<FlightInfoDTO> flights = new ArrayList<>();
        flights.add(createMockFlight("JFK", "LAX"));
        flights.add(createMockFlight("LAX", "SFO"));

        flightValidator.validateConnectingFlights(flights);
    }

    @Test
    void validateConnectingFlights_invalidSequence_shouldThrowException() {
        List<FlightInfoDTO> flights = new ArrayList<>();
        flights.add(createMockFlight("JFK", "LAX"));
        flights.add(createMockFlight("SFO", "ORD"));

        assertThatExceptionOfType(FlightSequenceException.class)
                .isThrownBy(() -> flightValidator.validateConnectingFlights(flights));
    }

    @Test
    void validateUniqueFlightNumbers_noDuplicateNumbers_shouldPass() throws DuplicateFlightNumberException {
        List<FlightInfoDTO> flights = new ArrayList<>();
        flights.add(createMockFlight("JFK", "LAX", "AA100"));
        flights.add(createMockFlight("LAX", "SFO", "AA101"));

        flightValidator.validateUniqueFlightNumbers(flights);
    }

    @Test
    void validateUniqueFlightNumbers_duplicateNumbers_shouldThrowException() {
        List<FlightInfoDTO> flights = new ArrayList<>();
        flights.add(createMockFlight("JFK", "LAX", "AA100"));
        flights.add(createMockFlight("LAX", "SFO", "AA100"));

        assertThatExceptionOfType(DuplicateFlightNumberException.class)
                .isThrownBy(() -> flightValidator.validateUniqueFlightNumbers(flights));
    }

    @Test
    void validateFlightDates_validDates_shouldPass() throws WrongFlightDatesException {
        List<FlightInfoDTO> flights = new ArrayList<>();
        flights.add(createMockFlight("JFK", "LAX", new Date(100000), new Date(200000)));
        flights.add(createMockFlight("LAX", "SFO", new Date(300000), new Date(400000)));

        flightValidator.validateFlightDates(flights);
    }

    @Test
    void validateFlightDates_invalidDates_shouldThrowException() {
        List<FlightInfoDTO> flights = new ArrayList<>();
        flights.add(createMockFlight("JFK", "LAX", new Date(200000), new Date(100000)));
        flights.add(createMockFlight("LAX", "SFO", new Date(400000), new Date(300000)));

        assertThatExceptionOfType(WrongFlightDatesException.class)
                .isThrownBy(() -> flightValidator.validateFlightDates(flights));
    }

    @Test
    void validateFlightSequence_validSequence_shouldPass() throws FlightSequenceException {
        List<FlightInfoDTO> flights = new ArrayList<>();
        flights.add(createMockFlight("JFK", "LAX", new Date(100000), new Date(200000)));
        flights.add(createMockFlight("LAX", "SFO", new Date(300000), new Date(400000)));

        flightValidator.validateFlightSequence(flights);
    }

    @Test
    void validateFlightSequence_invalidSequence_shouldThrowException() {
        List<FlightInfoDTO> flights = new ArrayList<>();
        flights.add(createMockFlight("JFK", "LAX", new Date(100000), new Date(200000)));
        flights.add(createMockFlight("LAX", "SFO", new Date(150000), new Date(400000)));  // Overlapping sequence

        assertThatExceptionOfType(FlightSequenceException.class)
                .isThrownBy(() -> flightValidator.validateFlightSequence(flights));
    }

    private ReservationSaveCaseDTO createMockReservation(String departingAirportCode, String arrivingAirportCode) {
        ReservationSaveCaseDTO reservation = mock(ReservationSaveCaseDTO.class);
        AirportDTO departingAirport = mock(AirportDTO.class);
        AirportDTO arrivingAirport = mock(AirportDTO.class);

        when(departingAirport.getAirportCode()).thenReturn(departingAirportCode);
        when(arrivingAirport.getAirportCode()).thenReturn(arrivingAirportCode);

        when(reservation.getDepartingAirport()).thenReturn(departingAirport);
        when(reservation.getArrivingAirport()).thenReturn(arrivingAirport);

        return reservation;
    }

    private FlightInfoDTO createMockFlight(String departureAirportCode, String arrivalAirportCode) {
        return createMockFlight(departureAirportCode, arrivalAirportCode, "FL123");
    }

    private FlightInfoDTO createMockFlight(String departureAirportCode, String arrivalAirportCode, String flightNumber) {
        return createMockFlight(departureAirportCode, arrivalAirportCode, flightNumber, new Date(), new Date(System.currentTimeMillis() + 100000));
    }

    private FlightInfoDTO createMockFlight(String departureAirportCode, String arrivalAirportCode, Date departureDate, Date arrivalDate) {
        return createMockFlight(departureAirportCode, arrivalAirportCode, "FL123", departureDate, arrivalDate);
    }

    private FlightInfoDTO createMockFlight(String departureAirportCode, String arrivalAirportCode, String flightNumber, Date departureDate, Date arrivalDate) {
        AirportDTO departureAirport = new AirportDTO();
        departureAirport.setAirportCode(departureAirportCode);
        AirportDTO arrivalAirport = new AirportDTO();
        arrivalAirport.setAirportCode(arrivalAirportCode);

        FlightInfoDTO flightInfoDTO = new FlightInfoDTO();
        flightInfoDTO.setDepartureAirport(departureAirport);
        flightInfoDTO.setArrivalAirport(arrivalAirport);
        flightInfoDTO.setFlightNumber(flightNumber);
        flightInfoDTO.setDepartureDate(departureDate);
        flightInfoDTO.setArrivalDate(arrivalDate);

        return flightInfoDTO;
    }

}

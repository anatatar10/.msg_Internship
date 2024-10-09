package com.calypso.binar.service.validation.casevalidation;

import com.calypso.binar.model.dto.FlightInfoDTO;
import com.calypso.binar.model.dto.ReservationSaveCaseDTO;
import com.calypso.binar.service.exception.DuplicateAirportException;
import com.calypso.binar.service.exception.DuplicateFlightNumberException;
import com.calypso.binar.service.exception.FlightSequenceException;
import com.calypso.binar.service.exception.WrongFlightDatesException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FlightValidator {
    public void uniqueAirportsValidator(ReservationSaveCaseDTO reservation, List<FlightInfoDTO> flights) throws DuplicateAirportException {

        Set<String> airportSet = new HashSet<>();

        // Add reservation airports to the set
        airportSet.add(reservation.getDepartingAirport().getAirportCode());
        airportSet.add(reservation.getArrivingAirport().getAirportCode());

        // Add flight airports to the set
        for (FlightInfoDTO flight : flights) {
            airportSet.add(flight.getDepartureAirport().getAirportCode());
            airportSet.add(flight.getArrivalAirport().getAirportCode());
        }

        // Check if the total number of unique airports matches the number of airports we added
        int totalAirportsCount = 2 + (flights.size()-1); // 2 from reservation + 2 for each flight (departing and destination)

        if (airportSet.size() != totalAirportsCount) {
            throw new DuplicateAirportException();
        }

    }

    public void validateConnectingFlights(List<FlightInfoDTO> flights) throws FlightSequenceException {

        for (int i = 0; i < flights.size() - 1; i++) {
            FlightInfoDTO currentFlight = flights.get(i);
            FlightInfoDTO nextFlight = flights.get(i + 1);
            if (!currentFlight.getArrivalAirport().getAirportCode().equals(nextFlight.getDepartureAirport().getAirportCode())) {
                throw new FlightSequenceException();
            }

        }

    }

    public void validateUniqueFlightNumbers(List<FlightInfoDTO> flights) throws DuplicateFlightNumberException {
        // Step 4: Ensure all flight numbers are unique
        Set<String> flightNumberSet = new HashSet<>();
        for (FlightInfoDTO flight : flights) {
            if (!flightNumberSet.add(flight.getFlightNumber())) {
                throw new DuplicateFlightNumberException();
            }
        }
    }

    public void validateFlightDates(List<FlightInfoDTO> flights) throws WrongFlightDatesException {
        for (FlightInfoDTO flight : flights) {
            if (flight.getArrivalDate().before(flight.getDepartureDate())) {
                throw new WrongFlightDatesException();
            }
        }
    }

    public void validateFlightSequence(List<FlightInfoDTO> flights) throws FlightSequenceException {
        for (int i = 0; i < flights.size() - 1; i++) {
            FlightInfoDTO currentFlight = flights.get(i);
            FlightInfoDTO nextFlight = flights.get(i + 1);

            if (currentFlight.getArrivalDate().after(nextFlight.getDepartureDate())) {
                throw new FlightSequenceException();
            }
        }
    }


}

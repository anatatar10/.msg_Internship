package com.calypso.binar.service;

import com.calypso.binar.model.FlightInfo;
import com.calypso.binar.model.dto.AirportDTO;
import com.calypso.binar.model.dto.FlightInfoDTO;
import com.calypso.binar.repository.FlightInfoRepository;
import com.calypso.binar.service.exception.FlightsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {
    @Autowired
    private FlightInfoRepository flightRepository;


    public List<FlightInfo> getAllFlights() {
        return flightRepository.findAll();
    }
    public void saveFlights(List<FlightInfo> flightInfoEntities) {
        flightRepository.saveAll(flightInfoEntities);
    }
    public void save(FlightInfo flightInfoEntity) {
        flightRepository.save(flightInfoEntity);
    }
    public List<FlightInfo> getAllFlightsForCase(String caseId) throws FlightsNotFoundException {
        Optional<List<FlightInfo>> flightInfoList = flightRepository.findAllByCaseId(caseId);

        if (flightInfoList.isEmpty() || flightInfoList.get().isEmpty()) {
            throw new FlightsNotFoundException();
        }

        return flightInfoList.get();
    }


    public List<FlightInfoDTO> getAllFlightsDTOForCase(String caseId) throws FlightsNotFoundException {
        Optional<List<FlightInfo>> flightInfoList = flightRepository.findAllByCaseId(caseId);

        if (flightInfoList.isEmpty() || flightInfoList.get().isEmpty()) {
            throw new FlightsNotFoundException();
        }

        return mapFlightInfoToDTO(flightInfoList.get());

    }

    private List<FlightInfoDTO> mapFlightInfoToDTO(List<FlightInfo> flightInfoList) {
        return flightInfoList.stream()
                .map(flight -> new FlightInfoDTO(
                        flight.getFlightInfoId(),
                        flight.getAirline(),
                        new AirportDTO(flight.getDepartingAirport().getAirportId(),flight.getDepartingAirport().getAirportCode(),
                                flight.getDepartingAirport().getAirportName(), flight.getDepartingAirport().isDeleted()),
                        new AirportDTO(flight.getDestinationAirport().getAirportId(),flight.getDestinationAirport().getAirportCode(),
                                flight.getDestinationAirport().getAirportName(), flight.getDestinationAirport().isDeleted()),
                        flight.getPlannedDepartureDate(),
                        flight.getPlannedArrivalDate(),


                        flight.getFlightNr(),

                        flight.isProblemFlight()
                ))
                .collect(Collectors.toList());
    }
}

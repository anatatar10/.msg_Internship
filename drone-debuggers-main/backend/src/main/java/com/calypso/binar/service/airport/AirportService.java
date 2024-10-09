package com.calypso.binar.service.airport;

import com.calypso.binar.model.Airport;
import com.calypso.binar.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {
    @Autowired
    private AirportRepository airportRepository;

    public List<Airport> getAllAirports() {
        return airportRepository.findAllActive();
    }

}

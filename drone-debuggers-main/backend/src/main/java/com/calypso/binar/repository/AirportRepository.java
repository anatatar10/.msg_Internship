package com.calypso.binar.repository;

import com.calypso.binar.model.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Integer> {
    @Query("SELECT a FROM Airport a WHERE a.deleted = false")
    List<Airport> findAllActive();

    Optional<Airport> findByAirportCode(String airportCode);
}

package com.calypso.binar.repository;

import com.calypso.binar.model.FlightInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightInfoRepository extends JpaRepository<FlightInfo, Integer> {
    @Query("SELECT fi FROM FlightInfo fi " +
            "JOIN Reservation r ON fi.reservation.reservationId = r.reservationId " +
            "JOIN Case cc ON r.reservationId = cc.reservation.reservationId " +
            "JOIN Airport departingAirport ON fi.departingAirport.airportId = departingAirport.airportId " +
            "JOIN Airport destinationAirport ON fi.destinationAirport.airportId = destinationAirport.airportId " +
            "WHERE cc.systemCaseId = :systemCaseId")
    Optional<List<FlightInfo>> findAllByCaseId(@Param("systemCaseId") String caseId);
}

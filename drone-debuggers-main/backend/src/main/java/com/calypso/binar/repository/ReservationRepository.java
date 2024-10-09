package com.calypso.binar.repository;

import com.calypso.binar.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r " +
            "JOIN Case cc ON r.reservationId = cc.reservation.reservationId " +
            "WHERE cc.systemCaseId = :systemCaseId")
    Optional<Reservation> findByCaseId(@Param("systemCaseId") String systemCaseId);
}

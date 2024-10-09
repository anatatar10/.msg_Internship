package com.calypso.binar.service;

import com.calypso.binar.model.Reservation;
import com.calypso.binar.model.dto.ReservationDTO;
import com.calypso.binar.repository.ReservationRepository;
import com.calypso.binar.service.exception.ReservationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public ReservationDTO getReservationForCase(String caseId) throws ReservationNotFoundException {
        Optional<Reservation> reservation =reservationRepository.findByCaseId(caseId);

        if (reservation.isEmpty()) {
            throw new ReservationNotFoundException();
        }

        return mapReservationToDTO(reservation.get());
    }


    private ReservationDTO mapReservationToDTO(Reservation reservation) {
        return new ReservationDTO(
                        reservation.getReservationId(),
                        reservation.getReservationNumber(),
                        reservation.getDepartingAirport(),
                        reservation.getDestinationAirport()
                );
    }
}

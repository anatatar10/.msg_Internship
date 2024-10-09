package com.calypso.binar.service;

import com.calypso.binar.model.Airport;
import com.calypso.binar.model.Reservation;
import com.calypso.binar.model.dto.ReservationDTO;
import com.calypso.binar.repository.ReservationRepository;
import com.calypso.binar.service.exception.ReservationNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void getReservationForCase_returnsReservationDTO_whenReservationExists() throws ReservationNotFoundException {
        // Given
        int caseId = 1;

        Reservation reservation = new Reservation();
        reservation.setReservationId(101);
        reservation.setReservationNumber("ABC123");
        reservation.setDepartingAirport(new Airport());
        reservation.setDestinationAirport(new Airport());

        // Mock the repository call
        when(reservationRepository.findByCaseId(String.valueOf(caseId))).thenReturn(Optional.of(reservation));

        // When
        ReservationDTO reservationDTO = reservationService.getReservationForCase(String.valueOf(caseId));

        // Then
        assertThat(reservationDTO).isNotNull();
        assertThat(reservationDTO.getReservationId()).isEqualTo(101);
        assertThat(reservationDTO.getReservationNumber()).isEqualTo("ABC123");
    }

    @Test
    public void getReservationForCase_throwsReservationNotFoundException_whenReservationDoesNotExist() {
        // Given
        int caseId = 1;

        // Mock the repository to return an empty Optional
        when(reservationRepository.findByCaseId(String.valueOf(caseId))).thenReturn(Optional.empty());

        // When & Then
        ReservationNotFoundException thrown = assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.getReservationForCase(String.valueOf(caseId));
        });

        assertThat(thrown).hasMessageContaining("Reservation not found");
    }
}


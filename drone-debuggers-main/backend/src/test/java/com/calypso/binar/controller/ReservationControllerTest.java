package com.calypso.binar.controller;

import com.calypso.binar.model.dto.ReservationDTO;
import com.calypso.binar.service.ReservationService;
import com.calypso.binar.service.exception.ReservationNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    public void getReservationsForCase_returnsReservationDTO_whenReservationExists() throws ReservationNotFoundException {
        // Given
        int caseId = 1;

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservationId(1001);
        reservationDTO.setReservationNumber("RES123456");

        // Mocking the service to return a ReservationDTO
        when(reservationService.getReservationForCase(String.valueOf(caseId))).thenReturn(reservationDTO);

        // When
        ResponseEntity<ReservationDTO> response = reservationController.getReservationsForCase(String.valueOf(caseId));

        // Then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getReservationId()).isEqualTo(1001);
        assertThat(response.getBody().getReservationNumber()).isEqualTo("RES123456");

        // Verify that the service method was called once with the expected caseId
        verify(reservationService, Mockito.times(1)).getReservationForCase(String.valueOf(caseId));
    }

    @Test
    public void getReservationsForCase_throwsReservationNotFoundException_whenReservationNotFound() throws ReservationNotFoundException {
        // Given
        int caseId = 1;

        // Mocking the service to throw ReservationNotFoundException
        when(reservationService.getReservationForCase(String.valueOf(caseId))).thenThrow(new ReservationNotFoundException());

        // When & Then
        ReservationNotFoundException thrown = assertThrows(ReservationNotFoundException.class, () -> {
            reservationController.getReservationsForCase(String.valueOf(caseId));
        });

        // Verify that the service method was called once with the expected caseId
        verify(reservationService, Mockito.times(1)).getReservationForCase(String.valueOf(caseId));

        // Assert the thrown exception message
        assertThat(thrown).hasMessageContaining("Reservation not found");


    }

}


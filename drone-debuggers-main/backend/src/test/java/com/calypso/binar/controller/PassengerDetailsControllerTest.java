package com.calypso.binar.controller;

import com.calypso.binar.model.dto.PassengerDetailsDTO;
import com.calypso.binar.service.PassengerDetailsService;
import com.calypso.binar.service.exception.PassengerDetailsNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassengerDetailsControllerTest {

    @Mock
    private PassengerDetailsService passengerDetailsService;

    @InjectMocks
    private PassengerDetailsController passengerDetailsController;

    @Test
    public void getPassengerDetailsByID_returnsPassengerDetails_whenDetailsExist() {
        // Given
        int id = 1;

        PassengerDetailsDTO passengerDetailsDTO = new PassengerDetailsDTO();
        passengerDetailsDTO.setPassengerDetailsId(id);
        passengerDetailsDTO.setFirstName("John");
        passengerDetailsDTO.setLastName("Doe");

        // Mocking the service to return a PassengerDetailsDTO
        when(passengerDetailsService.getPassengerDetailsById(id)).thenReturn(passengerDetailsDTO);

        // When
        ResponseEntity<PassengerDetailsDTO> response = passengerDetailsController.getPassengerDetailsByID(id);

        // Then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPassengerDetailsId()).isEqualTo(id);
        assertThat(response.getBody().getFirstName()).isEqualTo("John");
        assertThat(response.getBody().getLastName()).isEqualTo("Doe");

        // Verify that the service method was called once with the expected ID
        verify(passengerDetailsService, times(1)).getPassengerDetailsById(id);
    }

    @Test
    public void getPassengerDetailsForCase_returnsPassengerDetails_whenDetailsExist() throws PassengerDetailsNotFoundException {
        // Given
        int caseId = 1;

        PassengerDetailsDTO passengerDetailsDTO = new PassengerDetailsDTO();
        passengerDetailsDTO.setPassengerDetailsId(caseId);
        passengerDetailsDTO.setFirstName("Jane");
        passengerDetailsDTO.setLastName("Doe");

        // Mocking the service to return a PassengerDetailsDTO for a case
        when(passengerDetailsService.getPassengerDetailsForCase(String.valueOf(caseId))).thenReturn(passengerDetailsDTO);

        // When
        ResponseEntity<PassengerDetailsDTO> response = passengerDetailsController.getPassengerDetailsForCase(String.valueOf(caseId));

        // Then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPassengerDetailsId()).isEqualTo(caseId);
        assertThat(response.getBody().getFirstName()).isEqualTo("Jane");
        assertThat(response.getBody().getLastName()).isEqualTo("Doe");

        // Verify that the service method was called once with the expected caseId
        verify(passengerDetailsService, times(1)).getPassengerDetailsForCase(String.valueOf(caseId));
    }

    @Test
    public void getPassengerDetailsForCase_returnsNull_whenDetailsNotFound() throws PassengerDetailsNotFoundException {
        // Given
        int caseId = 1;

        // Mocking the service to return null when no passenger details are found
        when(passengerDetailsService.getPassengerDetailsForCase(String.valueOf(caseId))).thenReturn(null);

        // When
        ResponseEntity<PassengerDetailsDTO> response = passengerDetailsController.getPassengerDetailsForCase(String.valueOf(caseId));

        // Then
        // Verify that the service method was called once with the expected caseId
        verify(passengerDetailsService, times(1)).getPassengerDetailsForCase(String.valueOf(caseId));

        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody()).isNull();  // Assuming null when no details
    }

}

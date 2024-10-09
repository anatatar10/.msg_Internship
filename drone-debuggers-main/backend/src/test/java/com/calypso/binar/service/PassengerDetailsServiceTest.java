package com.calypso.binar.service;

import com.calypso.binar.model.PassengerDetails;
import com.calypso.binar.model.dto.PassengerDetailsDTO;
import com.calypso.binar.model.mapping.PassengerDetailsMapper;
import com.calypso.binar.repository.PassengerDetailsRepository;
import com.calypso.binar.service.exception.PassengerDetailsNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PassengerDetailsServiceTest {

    @Mock
    private PassengerDetailsRepository passengerDetailsRepository;

    @Mock
    private PassengerDetailsMapper passengerDetailsMapper;

    @InjectMocks
    private PassengerDetailsService passengerDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPassengerDetailsById_found() {
        int passengerId = 1;
        PassengerDetails passengerDetails = mock(PassengerDetails.class);
        PassengerDetailsDTO passengerDetailsDTO = mock(PassengerDetailsDTO.class);

        when(passengerDetailsRepository.findById(passengerId)).thenReturn(Optional.of(passengerDetails));
        when(passengerDetailsMapper.toPassengerDetailsDTO(passengerDetails)).thenReturn(passengerDetailsDTO);

        PassengerDetailsDTO result = passengerDetailsService.getPassengerDetailsById(passengerId);

        assertThat(result).isEqualTo(passengerDetailsDTO);
        verify(passengerDetailsRepository, times(1)).findById(passengerId);
        verify(passengerDetailsMapper, times(1)).toPassengerDetailsDTO(passengerDetails);
    }

    @Test
    void getPassengerDetailsById_notFound() {
        int passengerId = 1;

        when(passengerDetailsRepository.findById(passengerId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> passengerDetailsService.getPassengerDetailsById(passengerId));

        verify(passengerDetailsRepository, times(1)).findById(passengerId);
        verify(passengerDetailsMapper, never()).toPassengerDetailsDTO(any(PassengerDetails.class));
    }

    @Test
    void getPassengerDetailsForCase_found() throws PassengerDetailsNotFoundException {
        String caseId = "CASE123";
        PassengerDetails passengerDetails = mock(PassengerDetails.class);
        PassengerDetailsDTO passengerDetailsDTO = mock(PassengerDetailsDTO.class);

        when(passengerDetailsRepository.findByCaseId(caseId)).thenReturn(Optional.of(passengerDetails));
        when(passengerDetailsMapper.toPassengerDetailsDTO(passengerDetails)).thenReturn(passengerDetailsDTO);

        PassengerDetailsDTO result = passengerDetailsService.getPassengerDetailsForCase(caseId);

        assertThat(result).isEqualTo(passengerDetailsDTO);
        verify(passengerDetailsRepository, times(1)).findByCaseId(caseId);
        verify(passengerDetailsMapper, times(1)).toPassengerDetailsDTO(passengerDetails);
    }

    @Test
    void getPassengerDetailsForCase_notFound() {
        String caseId = "CASE123";

        when(passengerDetailsRepository.findByCaseId(caseId)).thenReturn(Optional.empty());

        passengerDetailsService.getPassengerDetailsForCase(caseId);
        verify(passengerDetailsRepository, times(1)).findByCaseId(caseId);
        verify(passengerDetailsMapper, never()).toPassengerDetailsDTO(any(PassengerDetails.class));
    }
}

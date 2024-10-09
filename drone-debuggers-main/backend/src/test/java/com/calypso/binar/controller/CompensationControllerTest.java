package com.calypso.binar.controller;

import com.calypso.binar.model.distance.DistanceRequest;
import com.calypso.binar.service.compensation.CompensationService;
import com.calypso.binar.service.exception.CompensationServiceException;
import com.calypso.binar.service.exception.DistanceCalculationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompensationControllerTest {

    @Mock
    private CompensationService compensationService;

    @InjectMocks
    private CompensationController compensationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void CalculateCompensation_success() throws CompensationServiceException, DistanceCalculationException {
        DistanceRequest distanceRequest = new DistanceRequest();
        distanceRequest.setFrom("JFK");
        distanceRequest.setTo("LAX");

        double expectedCompensation = 300.0;

        when(compensationService.calculateCompensation("JFK", "LAX")).thenReturn(expectedCompensation);

        ResponseEntity<Double> response = compensationController.calculateCompensation(distanceRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedCompensation);
        verify(compensationService, times(1)).calculateCompensation("JFK", "LAX");
    }

    @Test
    void CalculateCompensation_compensationServiceException() throws CompensationServiceException, DistanceCalculationException {
        DistanceRequest distanceRequest = new DistanceRequest();
        distanceRequest.setFrom("JFK");
        distanceRequest.setTo("LAX");

        when(compensationService.calculateCompensation("JFK", "LAX")).thenThrow(new CompensationServiceException());

        ResponseEntity<Double> response = compensationController.calculateCompensation(distanceRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(compensationService, times(1)).calculateCompensation("JFK", "LAX");
    }

    @Test
    void CalculateCompensation_distanceCalculationException() throws CompensationServiceException, DistanceCalculationException {
        DistanceRequest distanceRequest = new DistanceRequest();
        distanceRequest.setFrom("JFK");
        distanceRequest.setTo("LAX");

        when(compensationService.calculateCompensation("JFK", "LAX")).thenThrow(new DistanceCalculationException());

        ResponseEntity<Double> response = compensationController.calculateCompensation(distanceRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(compensationService, times(1)).calculateCompensation("JFK", "LAX");
    }
}

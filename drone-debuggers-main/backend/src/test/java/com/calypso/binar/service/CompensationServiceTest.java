package com.calypso.binar.service;

import com.calypso.binar.service.compensation.CompensationService;
import com.calypso.binar.service.exception.CompensationServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompensationServiceTest {


    @Value("${api.calculate.distance}")
    private String distanceApi;

    @Mock
    private RestTemplate restTemplate;


    @Spy
    private CompensationService compensationService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        compensationService.distanceApiUrl = "https://airportgap.com/api/airports/distance";
    }





    @Test
    public void calculateCompensation_Success() throws Exception {
        // Arrange
        double distance = 2000.0;
        doReturn(distance).when(compensationService).calculateDistance("JFK", "LAX");

        // Act
        double compensation = compensationService.calculateCompensation("JFK", "LAX");

        // Assert
        assertEquals(400, compensation, 0.01);
    }

    @Test
    public void calculateDistance_Failure_InvalidResponse() {
        // Arrange: Mock the RestTemplate to return an invalid JSON response
        try (MockedConstruction<RestTemplate> mockRestTemplate = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    String invalidResponseBody = "invalid_json";
                    ResponseEntity<String> responseEntity = new ResponseEntity<>(invalidResponseBody, HttpStatus.OK);
                    when(mock.postForEntity(eq("https://airportgap.com/api/airports/distance"),
                            any(HttpEntity.class), eq(String.class)))
                            .thenReturn(responseEntity);
                })) {

            // Act & Assert: Expect an exception due to invalid JSON
            assertThrows(CompensationServiceException.class, () -> {
                compensationService.calculateDistance("JFK", "LAX");
            });
        }
    }




    @Test
    public void testCalculateDistance_Failure_CompensationServiceException() {
        // Arrange: Simulate invalid JSON response from the API
        try (MockedConstruction<RestTemplate> mockRestTemplate = mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    ResponseEntity<String> responseEntity = new ResponseEntity<>("Invalid JSON", HttpStatus.OK);
                    when(mock.postForEntity(eq("https://airportgap.com/api/airports/distance"),
                            any(HttpEntity.class), eq(String.class)))
                            .thenReturn(responseEntity);
                })) {

            // Act & Assert: Check if CompensationServiceException is thrown
            assertThrows(CompensationServiceException.class, () -> {
                compensationService.calculateDistance("JFK", "LAX");
            });
        }
    }



    @Test
    public void testDetermineCompensation_LessThan1500Km() {
        // Act
        double compensation = compensationService.determineCompensation(1000);

        // Assert
        assertEquals(250, compensation);
    }

    @Test
    public void testDetermineCompensation_Between1500And3500Km() {
        // Act
        double compensation = compensationService.determineCompensation(2000);

        // Assert
        assertEquals(400, compensation);
    }

    @Test
    public void testDetermineCompensation_MoreThan3500Km() {
        // Act
        double compensation = compensationService.determineCompensation(4000);

        // Assert
        assertEquals(600, compensation);
    }
}





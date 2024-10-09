package com.calypso.binar.service;

import com.calypso.binar.model.Airport;
import com.calypso.binar.repository.AirportRepository;
import com.calypso.binar.service.airport.AirportAPIService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AirportAPIServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportAPIService airportAPIService;

    @Test
    public void initAirportDatabase_loadsDataSuccessfully_whenDataInApiExists() throws InterruptedException {
        // Mock the API response
        try (MockedConstruction<RestTemplate> mockRestTemplate = Mockito.mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    when(mock.getForObject(eq("https://MOCK/airportgap.com/api/airports"),
                            eq(String.class)))
                            .thenReturn(getMockResponsePage1());
                    when(mock.getForObject(
                            eq("https://MOCK/airportgap.com/api/airports?page=2"),
                            eq(String.class)))
                            .thenReturn(getMockResponsePage2());

                })) {

            // Define the expected list of Airport objects for page 1
            List<Airport> airportsPage = List.of(
                    new Airport("John F. Kennedy International Airport", "JFK"),
                    new Airport("Los Angeles International Airport", "LAX"),
                    new Airport("O'Hare International Airport", "ORD"),
                    new Airport("Dallas/Fort Worth International Airport", "DFW"),
                    new Airport("Denver International Airport", "DEN"),
                    new Airport("Hartsfield-Jackson Atlanta International Airport", "ATL")
            );

            // Mock the behavior of saveAll
            when(airportRepository.saveAll(ArgumentMatchers.eq(airportsPage))).thenReturn(airportsPage);

            ReflectionTestUtils.setField(airportAPIService, "fetchEnabled", true);
            ReflectionTestUtils.setField(airportAPIService, "apiUrl", "https://MOCK/airportgap.com/api/airports");

            airportAPIService.initAirportDatabase();

            // Verify the interactions
            verify(airportRepository).saveAll(ArgumentMatchers.eq(airportsPage));
        }
    }

    private String getMockResponsePage1() {
        return "{"
                + "\"data\": ["
                + "  {\"attributes\": {\"iata\": \"JFK\", \"name\": \"John F. Kennedy International Airport\"}},"
                + "  {\"attributes\": {\"iata\": \"LAX\", \"name\": \"Los Angeles International Airport\"}},"
                + "  {\"attributes\": {\"iata\": \"ORD\", \"name\": \"O'Hare International Airport\"}}"
                + "],"
                + "\"links\": {"
                + "  \"self\": \"https://MOCK/airportgap.com/api/airports\","
                + "  \"next\": \"https://MOCK/airportgap.com/api/airports?page=2\","
                + "  \"last\": \"https://MOCK/airportgap.com/api/airports?page=2\""
                + "}"
                + "}";
    }

    private String getMockResponsePage2() {
        return "{"
                + "\"data\": ["
                + "  {\"attributes\": {\"iata\": \"DFW\", \"name\": \"Dallas/Fort Worth International Airport\"}},"
                + "  {\"attributes\": {\"iata\": \"DEN\", \"name\": \"Denver International Airport\"}},"
                + "  {\"attributes\": {\"iata\": \"ATL\", \"name\": \"Hartsfield-Jackson Atlanta International Airport\"}}"
                + "],"
                + "\"links\": {"
                + "  \"self\": \"https://MOCK/airportgap.com/api/airports?page=2\","
                + "  \"next\": \"https://MOCK/airportgap.com/api/airports\","
                + "  \"last\": \"https://MOCK/airportgap.com/api/airports?page=2\""
                + "}"
                + "}";
    }

}

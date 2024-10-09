package com.calypso.binar.service.airport;

import com.calypso.binar.model.Airport;
import com.calypso.binar.repository.AirportRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@EnableAsync
@Service
public class AirportAPIService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirportAPIService.class);

    @Autowired
    private AirportRepository airportRepository;

    @Value("${airport.fetch.enabled}")
    private boolean fetchEnabled;

    @Value("${airport.fetch.url}")
    private String apiUrl;
    private static final long PAUSE_DURATION = 600; // in milliseconds


    @Scheduled(initialDelayString = "${airport.fetch.delay}",fixedRateString="${airport.fetch.interval}")
    @Transactional
    @Async
    public void initAirportDatabase() {
        if (fetchEnabled) {
            try {
                LOGGER.info("Fetching and initializing airports...");
                List<Airport> totalAirports = fetchAirports();
                syncAirports(totalAirports);

                LOGGER.info("Airport fetching and initialization completed.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("Initialization interrupted: {}", e.getMessage());
            }
        } else {
            LOGGER.info("Airport fetching is disabled. Skipping initialization.");
        }
    }

    @Autowired
    public AirportAPIService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Transactional
    public void syncAirports(List<Airport> fetchedAirports) {
        List<Airport> existingAirports = airportRepository.findAll();
        List<Airport> newAirports = new ArrayList<>();
        List<Airport> deletedAirports = new ArrayList<>(existingAirports); // Initialize with all current airports

        Map<String, Airport> currentAirportMap = existingAirports.stream()
                .collect(Collectors.toMap(Airport::getAirportCode, airport -> airport));

        // Iterate through the fetched airports
        for (Airport fetchedAirport : fetchedAirports) {
            Airport databaseAirport = currentAirportMap.get(fetchedAirport.getAirportCode());
            // Check if the airport already exists in the database
            if (databaseAirport == null) {
                newAirports.add(fetchedAirport);
            } else {
                // Existing airport found
                deletedAirports.remove(databaseAirport); // It's not deleted, so remove from the deletion list
                if (!fetchedAirport.getAirportName().equals(databaseAirport.getAirportName())) {
                    databaseAirport.setAirportName(fetchedAirport.getAirportName());
                }
            }
        }

        // Save the new airports to the database
        airportRepository.saveAll(newAirports);
        for(Airport deletedAirport : deletedAirports){
            if(!deletedAirport.isDeleted()) {
                deletedAirport.setDeleted(true);
            }
        }

    }


    private List<Airport> fetchAirports() throws InterruptedException {
        String nextPageUrl = apiUrl;
        List<Airport> airports = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        // Log the apiUrl to debug
        LOGGER.info("Using API URL: {}", apiUrl);

        do {
            String rawResponse = fetchRawApiResponse(restTemplate, nextPageUrl);

            // Extract the next page URL from the response
            nextPageUrl = extractNextPageUrl(rawResponse);
            airports.addAll(extractAirports(rawResponse));
            // Pause after processing each request
            TimeUnit.MILLISECONDS.sleep(PAUSE_DURATION);
        } while (!Objects.equals(nextPageUrl, apiUrl));

        return airports;
    }


    private String fetchRawApiResponse(RestTemplate restTemplate, String url) {
        try {
            // Fetch the response as a raw JSON string
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            LOGGER.error("Error fetching data from API: {}", e.getMessage());
            return "";
        }
    }

    private String extractNextPageUrl(String rawResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the JSON response into a JsonNode tree
            JsonNode root = objectMapper.readTree(rawResponse);

            // Extract the "next" URL directly
            JsonNode nextUrlNode = root.path("links").path("next");
            return nextUrlNode.asText(null); // Return the URL as text, or null if not present
        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing JSON response: {}", e.getMessage());
            return null;
        }
    }

    private List<Airport> extractAirports(String rawResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Airport> airports = new ArrayList<>();

        try {
            // Parse the JSON response into a JsonNode tree
            JsonNode root = objectMapper.readTree(rawResponse);

            // Navigate to the array of airports
            JsonNode dataNode = root.path("data");

            // Iterate through each airport object in the array
            for (JsonNode airportNode : dataNode) {
                JsonNode attributesNode = airportNode.path("attributes");

                Airport airport = new Airport();
                airport.setAirportCode(attributesNode.path("iata").asText(null));
                airport.setAirportName(attributesNode.path("name").asText(null));

                airports.add(airport);
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing JSON response: {}", e.getMessage());
        }

        return airports;
    }

}

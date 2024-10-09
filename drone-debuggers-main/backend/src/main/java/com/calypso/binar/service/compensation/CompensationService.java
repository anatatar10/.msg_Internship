package com.calypso.binar.service.compensation;

import com.calypso.binar.model.distance.DistanceRequest;
import com.calypso.binar.security.JwtService;
import com.calypso.binar.service.exception.CompensationServiceException;
import com.calypso.binar.service.exception.DistanceCalculationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class CompensationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);


    @Value("${api.calculate.distance}")
    public String distanceApiUrl;


    @Autowired
    private ObjectMapper objectMapper;


    /**
     * this is the main function that we call to calculate the compensation based on those parameters
     * @param from IATA code for the starting airport
     * @param to   Iata Code for the arriving airport
     * @return     returns the compensation already calculated
     */

    public double calculateCompensation(String from, String to) throws CompensationServiceException, DistanceCalculationException {
        double kilometers = calculateDistance(from, to);
        return determineCompensation(kilometers);
    }

    /**
     * in this function we calculate the distance. we make a call to the api, then we make an object
     * of the model distance request which contains the IATA codes, we initialize the Http headers used for
     * adding different headers to the request.
     * Http Entity is the object that will be sent in the http request. is the combination of distance request and the
     * headers
     * we send the POST request with rest template. we get the answer in the form of ResponseEntity<String>
     *
     * @param from IATA code for the starting airport
     * @param to   Iata Code for the arriving airport
     * @return  returns the distance in kilometers
     */
    public double calculateDistance(String from, String to) throws CompensationServiceException, DistanceCalculationException {
        String url = distanceApiUrl;

        RestTemplate restTemplate = new RestTemplate();

        DistanceRequest requestBody = new DistanceRequest();
        requestBody.setFrom(from);
        requestBody.setTo(to);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<DistanceRequest> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                LOGGER.info("Received response body: {}", response.getBody());
                JsonNode root = objectMapper.readTree(response.getBody());
                double kilometers = root.path("data").path("attributes").path("kilometers").asDouble();
                LOGGER.info("Distance in kilometers: {}", kilometers);
                return kilometers;
            } catch (Exception e) {
                throw new CompensationServiceException();
            }
        } else {
            throw new DistanceCalculationException();
        }
    }

    /**
     * we determine the different compensations based on the distance of the flight
     * @param kilometers is the distance that we take
     * @return returns the compensation
     */

    public double determineCompensation(double kilometers) {
        if (kilometers < 1500) {
            return 250;
        } else if (kilometers < 3500) {
            return 400;
        } else {
            return 600;
        }
    }
}

package com.calypso.binar.controller;

import com.calypso.binar.model.distance.DistanceRequest;
import com.calypso.binar.service.compensation.CompensationService;
import com.calypso.binar.service.exception.CompensationServiceException;
import com.calypso.binar.service.exception.DistanceCalculationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/compensation", "/api/compensation/"})
public class CompensationController {

    @Autowired
    public CompensationService compensationService;

    /**
     *
     * @param distanceRequest
     * @return
     */
    @PostMapping("/calculate")
    public ResponseEntity<Double> calculateCompensation(@RequestBody DistanceRequest distanceRequest) throws CompensationServiceException, DistanceCalculationException {
        try{
            double compensation = compensationService.calculateCompensation(distanceRequest.getFrom(), distanceRequest.getTo());
            return ResponseEntity.ok(compensation);
        } catch (CompensationServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DistanceCalculationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}

package com.calypso.binar.service.validation.casevalidation;

import com.calypso.binar.service.compensation.CompensationService;
import com.calypso.binar.service.exception.CaseValidationException;
import com.calypso.binar.service.exception.CompensationServiceException;
import com.calypso.binar.service.exception.DistanceCalculationException;
import org.springframework.stereotype.Component;

@Component
public class CompensationValidator {
    private final CompensationService compensationService;

    public CompensationValidator(CompensationService compensationService) {
        this.compensationService = compensationService;
    }

    public void validateCompensation(int compensation, String departingAirport, String destinationAirport) throws CompensationServiceException, DistanceCalculationException, CaseValidationException {
        // Calculăm compensația corectă pe baza distanței dintre aeroporturi
        double calculatedCompensation = compensationService.calculateCompensation(departingAirport, destinationAirport);

        // Comparăm compensația calculată cu cea furnizată
        if (compensation != (int) calculatedCompensation) {
            throw new CaseValidationException("The compensation value provided does not match the calculated value based on the flight distance.");
        }
    }

}

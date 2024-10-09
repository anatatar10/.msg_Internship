package com.calypso.binar.service.validation;

import com.calypso.binar.model.dto.AttachedFilesDTO;
import com.calypso.binar.model.dto.CaseDTO;
import com.calypso.binar.service.exception.*;
import com.calypso.binar.service.validation.casevalidation.DisruptionValidator;
import com.calypso.binar.service.validation.casevalidation.FileValidator;
import com.calypso.binar.service.validation.casevalidation.FlightValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaseValidator {

    @Autowired
    private DisruptionValidator disruptionValidator;
    @Autowired
    private FlightValidator flightValidator;
    @Autowired
    private FileValidator fileValidator;


    public void validateCase(CaseDTO compensationCase) throws DescriptionTooLongException, CaseInvalidException, DuplicateAirportException, WrongFlightDatesException, FlightSequenceException, DuplicateFlightNumberException {
        disruptionValidator.validateIncidentDescription(compensationCase.getDisruption());
        disruptionValidator.validateEligibility(compensationCase.getDisruption());

        flightValidator.uniqueAirportsValidator(compensationCase.getReservationData(), compensationCase.getFlightsInfo());
        flightValidator.validateFlightDates(compensationCase.getFlightsInfo());
        flightValidator.validateConnectingFlights(compensationCase.getFlightsInfo());
        flightValidator.validateUniqueFlightNumbers(compensationCase.getFlightsInfo());
        flightValidator.validateFlightSequence(compensationCase.getFlightsInfo());

    }

    public void validateFiles(AttachedFilesDTO[] attachedFiles) throws FileTooLargeException {
        for( AttachedFilesDTO file : attachedFiles){
            fileValidator.isFileSizeValid(file);
        }
    }
}

package com.calypso.binar.service.validation.casevalidation;

import com.calypso.binar.model.dto.DisruptionDTO;
import com.calypso.binar.service.exception.CaseInvalidException;
import com.calypso.binar.service.exception.DescriptionTooLongException;
import org.springframework.stereotype.Component;

@Component
public class DisruptionValidator {

    // Constants for incident description validation
    private static final int MAX_DESCRIPTION_LENGTH = 1024;

    // Constants for disruption types
    private static final String CANCELLATION = "Cancellation";
    private static final String DELAY = "Delay";
    private static final String DENIED_BOARDING = "Denied Boarding";

    // Constants for disruption options
    private static final String LESS_THAN_14_DAYS = "<14 days";
    private static final String ON_FLIGHT_DAY = "on flight day";
    private static final String MORE_THAN_3_HOURS = ">3 hours";
    private static final String NEVER_ARRIVED = "Never Arrived";

    public void validateIncidentDescription(DisruptionDTO disruption) throws DescriptionTooLongException {
        if (disruption.getIncidentDescription() != null && disruption.getIncidentDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new DescriptionTooLongException();
        }
    }

    public void validateEligibility(DisruptionDTO disruption) throws CaseInvalidException {
        String disruptionType = disruption.getCancellationType().getCancellationTypeDescription();
        String disruptionOption = disruption.getDisruptionOption().getDisruptionOptionDescription();

        boolean isValid = false;

        switch (disruptionType) {
            case CANCELLATION:
                isValid = LESS_THAN_14_DAYS.equals(disruptionOption)
                        || ON_FLIGHT_DAY.equals(disruptionOption);
                break;

            case DELAY:
                isValid = MORE_THAN_3_HOURS.equals(disruptionOption)
                        || NEVER_ARRIVED.equals(disruptionOption);
                break;

            case DENIED_BOARDING:
                if (disruptionOption != null) {
                    isValid = true;
                }
                break;

            default:
                break;
        }

        if (!isValid) {
            throw new CaseInvalidException("Case not eligible");
        }
    }
}
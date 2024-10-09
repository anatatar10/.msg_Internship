package com.calypso.binar.service.savecase;

import com.calypso.binar.model.*;
import com.calypso.binar.model.dto.*;
import com.calypso.binar.model.mapping.FlightInfoMapper;
import com.calypso.binar.repository.*;
import com.calypso.binar.service.FlightService;
import com.calypso.binar.service.SystemCaseIdGenerator;
import com.calypso.binar.service.UserService;
import com.calypso.binar.service.compensation.CompensationService;
import com.calypso.binar.service.exception.*;
import com.calypso.binar.service.validation.CaseValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class SaveCaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveCaseService.class);

    // Constants for status values
    private static final String STATUS_INCOMPLETE = "INCOMPLETE";
    private static final String STATUS_NEW = "NEW";

    // Error messages
    private static final String ERROR_STATUS_NON_EXISTENT = "Status not existent: ";
    private static final String ERROR_CANCELLATION_TYPE_NOT_FOUND = "Cancellation type not found.";
    private static final String ERROR_AIRLINE_MOTIVE_NOT_FOUND = "Airline Motive not found.";
    private static final String ERROR_DISRUPTION_OPTION_NOT_FOUND = "Disruption Option not found.";
    private static final String ERROR_COMPENSATION_MISMATCH = "Compensation mismatch: Front-end compensation does not match backend calculation.";
    private static final String ERROR_INVALID_STATUS_PROVIDED = "Invalid status provided.";
    private static final String ERROR_SYSTEM_CASE_ID_NOT_PRESENT = "System case ID not present: ";


    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FlightInfoMapper flightInfoMapper;

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private CaseValidator caseValidatorService;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private FlightService flightService;

    @Autowired
    private SystemCaseIdGenerator systemCaseIdGenerator;

    @Autowired
    private CancellationTypeRepository cancellationTypeRepository;

    @Autowired
    private DisruptionOptionRepository disruptionOptionRepository;

    @Autowired
    private AirlineMotiveRepository airlineMotiveRepository;

    @Autowired
    private DisruptionRepository disruptionRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PassengerDetailsRepository passengerDetailsRepository;


    @Transactional
    public String saveCase(CaseDTO caseDTO) throws CompensationServiceException, DistanceCalculationException, StatusNonExistent, SystemCaseIdNotPresent, InvalidEmailException, DuplicateAirportException, WrongFlightDatesException, DescriptionTooLongException, DuplicateFlightNumberException, FlightSequenceException, CaseInvalidException {
        LOGGER.debug("Received CaseDTO: {}", caseDTO);

        String statusName = caseDTO.getStatus().getStatusName();
        Optional<Status> statusFromDB = statusRepository.findByStatusName(statusName);

        if (statusFromDB.isEmpty()) {
            throw new StatusNonExistent(ERROR_STATUS_NON_EXISTENT + statusName);
        }

        caseValidatorService.validateCase(caseDTO);

        if (STATUS_INCOMPLETE.equals(caseDTO.getStatus().getStatusName())) {
            // Handling INCOMPLETE status: create a case with incomplete details
            Case newCase = new Case();
            newCase.setStatus(statusFromDB.get());
            newCase.setDateCreated(new Timestamp(System.currentTimeMillis()));

            Optional<User> userDB = userService.getUserByEmail(caseDTO.getPassenger().getEmail());

            if(userDB.isEmpty()) {
                throw new InvalidEmailException(caseDTO.getPassenger().getEmail());
            }

            newCase.setPassenger(userDB.get());


            //Save and Set reservation
            Reservation reservation = saveReservation(caseDTO.getReservationData());

            newCase.setReservation(reservation);

            //Set SystemCaseId
            String systemCaseId = systemCaseIdGenerator.generateSystemCaseId();
            newCase.setSystemCaseId(systemCaseId);

            //Save disruption data
            Disruption disruption;
            disruption = saveDisruption(caseDTO.getDisruption());
            disruptionRepository.save(disruption);
            newCase.setDisruption(disruption);

            //Save flights
            saveFlights(caseDTO.getFlightsInfo(), reservation);


            //Set compensation
            double frontEndCompensation = caseDTO.getReservationData().getPossibleCompensation();

            double calculatedCompensation = checkCompensation(caseDTO.getReservationData(), frontEndCompensation);

            newCase.setCompensation((int) calculatedCompensation);

            this.caseRepository.save(newCase);
            return systemCaseId;

        } else if (STATUS_NEW.equals(caseDTO.getStatus().getStatusName())) {
            // Handling NEW status: update an existing incomplete case with complete details

            Case existingIncompleteCase = caseRepository.findBySystemCaseId(caseDTO.getSystemCaseId())
                    .orElseThrow(() -> new SystemCaseIdNotPresent(ERROR_SYSTEM_CASE_ID_NOT_PRESENT + caseDTO.getSystemCaseId()));

            // Update the incomplete case with new data
            existingIncompleteCase.setStatus(statusFromDB.get());

            PassengerDetailsDTO passengerDetailsDTO = caseDTO.getPassengerDetails();
            User passengerComplete = existingIncompleteCase.getPassenger();

            PassengerDetails passengerDetails= savePassengerDetails(passengerDetailsDTO, passengerComplete);

            this.passengerDetailsRepository.save(passengerDetails);
            existingIncompleteCase.setPassengerDetails(passengerDetails);

            caseRepository.save(existingIncompleteCase);
            return "{\"response\":\"Case created successfully\"}";
        } else {
            throw new CaseInvalidException(ERROR_INVALID_STATUS_PROVIDED);
        }
    }

    public PassengerDetails savePassengerDetails(PassengerDetailsDTO passengerDetailsDTO, User passenger) {
        PassengerDetails passengerDetails = new PassengerDetails(passengerDetailsDTO.getDateOfBirth(), passengerDetailsDTO.getPhoneNumber(),
                passengerDetailsDTO.getAddress(), passengerDetailsDTO.getPostalCode(), passenger.getEmail(),
                passenger.getFirstName(), passenger.getLastName());

        this.passengerDetailsRepository.save(passengerDetails);

        return passengerDetails;
    }

    public double checkCompensation(ReservationSaveCaseDTO reservationDTO, double frontEndCompensation) throws CompensationServiceException, DistanceCalculationException, CaseInvalidException {
        double calculatedCompensation = compensationService.calculateCompensation(
                reservationDTO.getDepartingAirport().getAirportCode(),
                reservationDTO.getArrivingAirport().getAirportCode());

        if (frontEndCompensation != calculatedCompensation) {
            throw new CaseInvalidException(ERROR_COMPENSATION_MISMATCH);
        }

        return calculatedCompensation;
    }

    public void saveFlights(List<FlightInfoDTO> flightInfoDTOS, Reservation reservation) {
        List<FlightInfo> flightInfoEntities = flightInfoDTOS.stream()
                .map(flightInfoDTO -> {
                    FlightInfo flightInfo = flightInfoMapper.toFlightInfoEntity(flightInfoDTO);
                    flightInfo.setReservation(reservation);
                    flightInfo.setDepartingAirport(
                            this.airportRepository.findByAirportCode(flightInfo.getDepartingAirport().getAirportCode()).orElseThrow(() -> new RuntimeException("Departing airport not found")));
                    flightInfo.setDestinationAirport(
                            this.airportRepository.findByAirportCode(flightInfo.getDestinationAirport().getAirportCode()).orElseThrow(() -> new RuntimeException("Destination airport not found")));
                    return flightInfo;
                })
                .toList();

        flightService.saveFlights(flightInfoEntities);
    }


    public Disruption saveDisruption(DisruptionDTO disruptionDTO) throws CaseInvalidException {
        Disruption disruption = new Disruption();
        if (disruptionDTO.getCancellationType() != null) {
            CancellationType cancellationType = cancellationTypeRepository.findByCancellationTypeDescription(
                            disruptionDTO.getCancellationType().getCancellationTypeDescription())
                    .orElseThrow(() -> new CaseInvalidException(ERROR_CANCELLATION_TYPE_NOT_FOUND));
            disruption.setCancellationType(cancellationType);
        }

        if (disruptionDTO.getAirlineMotive() != null) {
            String airlineMotiveTypeDescription = disruptionDTO.getAirlineMotive().getAirlineMotiveTypeDescription();
            AirlineMotive airlineMotive = airlineMotiveRepository.findByAirlineMotiveTypeDescription(airlineMotiveTypeDescription)
                    .orElseThrow(() -> new CaseInvalidException(ERROR_AIRLINE_MOTIVE_NOT_FOUND));
            disruption.setAirlineMotive(airlineMotive);
        }

        if (disruptionDTO.getDisruptionOption() != null) {
            DisruptionOption disruptionOption = disruptionOptionRepository.findByDisruptionOptionDescription(
                            disruptionDTO.getDisruptionOption().getDisruptionOptionDescription())
                    .orElseThrow(() -> new CaseInvalidException(ERROR_DISRUPTION_OPTION_NOT_FOUND));
            disruption.setDisruptionOption(disruptionOption);
        }

        disruption.setIncidentDescription(disruptionDTO.getIncidentDescription());

        return disruption;
    }

    public Reservation saveReservation(ReservationSaveCaseDTO reservationSaveCaseDTO){
        Optional<Airport> departingAirport = this.airportRepository.findByAirportCode(reservationSaveCaseDTO.getDepartingAirport().getAirportCode());
        Optional<Airport> destinationAirport = this.airportRepository.findByAirportCode(reservationSaveCaseDTO.getArrivingAirport().getAirportCode());

        Reservation reservation = new Reservation(reservationSaveCaseDTO.getReservationNumber(), departingAirport.get(), destinationAirport.get());

        this.reservationRepository.save(reservation);

        return reservation;
    }
}

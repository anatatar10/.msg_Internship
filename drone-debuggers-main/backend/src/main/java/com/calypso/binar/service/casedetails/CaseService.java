package com.calypso.binar.service.casedetails;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.Status;
import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.CaseDTO;
import com.calypso.binar.model.dto.CaseListDTO;
import com.calypso.binar.model.dto.CaseSummaryDTO;
import com.calypso.binar.model.dto.ColleagueDTO;
import com.calypso.binar.model.mapping.CaseMapper;
import com.calypso.binar.model.mapping.UserMapper;
import com.calypso.binar.model.dto.PastCaseDTO;
import com.calypso.binar.repository.CaseRepository;
import com.calypso.binar.repository.StatusRepository;
import com.calypso.binar.repository.UserRepository;
import com.calypso.binar.service.StatusService;
import com.calypso.binar.service.exception.CaseNotFoundException;
import com.calypso.binar.service.exception.StatusNonExistent;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CaseService {

    private static final String CASE_NOT_FOUND = "Case Not Found";



    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private StatusService statusService;



    @Autowired
    private CaseMapper caseMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StatusRepository statusRepository;

    public List<Case> getAllCases() {
        return caseRepository.findAll();
    }

    /**
     * Get all cases from database with reservation number and status case
     */
    public List<CaseListDTO> getAllCasesForAdmin() {
        return caseRepository.findAll().stream()
                .map(this::mapToCaseListDTO)
                .collect(Collectors.toList());
    }


    private CaseListDTO mapToCaseListDTO(Case caseEntity)  {
        return new CaseListDTO(
                caseEntity.getCaseId(),
                caseEntity.getSystemCaseId(),
                caseEntity.getReservation().getReservationNumber(),
                caseEntity.getStatus().getStatusName()
        );
    }


    public void deleteCaseById(Integer id) throws CaseNotFoundException {
        if(id == null || id < 0) {
            throw new IllegalArgumentException("Case id cannot be null or negative");
        }
        Optional<Case> caseOptional = caseRepository.findById(id);
        if(caseOptional.isEmpty()) {
            throw new CaseNotFoundException("Case with id " + id + " does not exist");
        }
        caseRepository.deleteById(id);
    }


    public Case getCaseBySystemCaseId(String systemCaseId) throws CaseNotFoundException {
        Optional<Case> compensationCase= caseRepository.findBySystemCaseId(systemCaseId);
        if(compensationCase.isEmpty())
            throw new CaseNotFoundException("Case with systemCaseId " + systemCaseId + " does not exist");

        return compensationCase.get();
    }

    public CaseDTO getCaseSummaryById(int caseId) {
        Case caseEntity = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException(CASE_NOT_FOUND));
        return caseMapper.toCaseDTO(caseEntity);
    }



    @Transactional
    public void assignColleague(String systemCaseId, String assignedColleagueEmail) {
        Case caseEntity = caseRepository.findBySystemCaseId(systemCaseId)
                .orElseThrow(() -> new ServiceException(CASE_NOT_FOUND));

        if (assignedColleagueEmail == null || assignedColleagueEmail.isEmpty()) {
            caseEntity.setColleague(null);
            Status newStatus = statusRepository.findByStatusName("NEW")
                    .orElseThrow(() -> new ServiceException("Status 'NEW' not found"));
            caseEntity.setStatus(newStatus);
        } else {
            User colleague = userRepository.findByEmail(assignedColleagueEmail)
                    .orElseThrow(() -> new ServiceException("Colleague not found"));
            caseEntity.setColleague(colleague);
            Status assignedStatus = statusRepository.findByStatusName("ASSIGNED")
                    .orElseThrow(() -> new ServiceException("Status 'ASSIGNED' not found"));
            caseEntity.setStatus(assignedStatus);
        }

        caseRepository.save(caseEntity);
    }




    public List<ColleagueDTO> getAllColleagues() {
        List<User> colleagues = userRepository.findByRoleName("COLLEAGUE");

        if (colleagues == null || colleagues.isEmpty()) {
            return Collections.emptyList();
        }


        return colleagues.stream()
                .map(user -> new ColleagueDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getRole().getRoleName()
                ))
                .collect(Collectors.toList());
    }



    @Transactional
    public void updateCaseStatus(String systemCaseId, String newStatusName) {
        Case caseEntity = caseRepository.findBySystemCaseId(systemCaseId)
                .orElseThrow(() -> new ServiceException(CASE_NOT_FOUND));

        Status status = statusRepository.findByStatusName(newStatusName)
                .orElseThrow(() -> new ServiceException("Status not found"));

        caseEntity.setStatus(status);

        caseRepository.save(caseEntity);
    }

    public List<CaseSummaryDTO> getAllCaseSummaries() {
        List<Case> cases = caseRepository.findAll();

        if(cases.isEmpty()) {
            return List.of();
        }

        return this.mapToCaseSummaryDTO(cases);

    }

    public List<CaseSummaryDTO> mapToCaseSummaryDTO(List<Case> cases) {
        return cases.stream().map(caseEntity -> {
            CaseSummaryDTO caseSummaryDTO = new CaseSummaryDTO();

            caseSummaryDTO.setSystemCaseId(caseEntity.getSystemCaseId());
            caseSummaryDTO.setCaseDate(caseEntity.getDateCreated());
            caseSummaryDTO.setFlightNumber(caseEntity.getReservation().getReservationNumber());
            caseSummaryDTO.setFirstPassengerName(caseEntity.getPassenger().getFirstName());
            caseSummaryDTO.setLastPassengerName(caseEntity.getPassenger().getLastName());
            caseSummaryDTO.setStatus(caseEntity.getStatus().getStatusName());
            if(caseEntity.getColleague() != null) {
                ColleagueDTO colleagueDTO = new ColleagueDTO();
                colleagueDTO.setEmail(caseEntity.getColleague().getEmail());
                colleagueDTO.setFirstName(caseEntity.getColleague().getFirstName());
                colleagueDTO.setLastName(caseEntity.getColleague().getLastName());
                caseSummaryDTO.setAssignedColleagueDTO(colleagueDTO);
            }else
            {
                return caseSummaryDTO;
            }


            return caseSummaryDTO;
        }).collect(Collectors.toList());
    }




    /**
     * Retrieves a list of past cases for a passenger based on their ID.
     * The cases are filtered to include only those with specific statuses ("INVALID" and "VALID").
     *
     * @param id the ID of the passenger whose past cases are being retrieved
     * @return a list of PastCaseDTO representing the passenger's past cases
     */
    public List<PastCaseDTO> getPastCasesPassenger(int id) throws StatusNonExistent {
        Status invalidStatus = statusService.getStatusByStatusName("INVALID");
        Status validStatus = statusService.getStatusByStatusName("VALID");
        Optional<List<Case>> cases = caseRepository.findPastCasesByPassengerId(id,Arrays.asList(invalidStatus.getStatusName(), validStatus.getStatusName()));

        if(cases.isEmpty()) {
            cases = Optional.of(List.of());
        }
        return this.mapToPastCaseDTO(cases.get());

    }


    /**
     * Retrieves a list of past cases for a passenger based on their ID.
     * The cases are filtered to include only those with specific statuses ("INVALID" and "VALID").
     *
     * @param id the ID of the passenger whose past cases are being retrieved
     * @return a list of PastCaseDTO representing the passenger's past cases
     */
    public List<PastCaseDTO> getActiveCasesPassenger(int id) throws StatusNonExistent {
        Status invalidStatus = statusService.getStatusByStatusName("INVALID");
        Status validStatus = statusService.getStatusByStatusName("VALID");
        Optional<List<Case>> cases = caseRepository.findActiveCasesByPassengerId(id,Arrays.asList(invalidStatus.getStatusName(), validStatus.getStatusName()));

        if(cases.isEmpty()) {
            cases = Optional.of(List.of());
        }
        return this.mapToPastCaseDTO(cases.get());

    }

    public List<PastCaseDTO> mapToPastCaseDTO(List<Case> cases) {
        return cases.stream().map(caseEntity -> {
            PastCaseDTO pastCaseDTO = new PastCaseDTO();

            // Directly map fields from Case to PastCaseDTO
            pastCaseDTO.setCaseId(caseEntity.getCaseId());
            pastCaseDTO.setSystemCaseId(caseEntity.getSystemCaseId());
            pastCaseDTO.setReservationNumber(caseEntity.getReservation().getReservationNumber());
            pastCaseDTO.setPassengerFirstName(caseEntity.getPassenger().getFirstName());
            pastCaseDTO.setPassengerLastName(caseEntity.getPassenger().getLastName());
            pastCaseDTO.setStatus(caseEntity.getStatus().getStatusName());
            if(caseEntity.getColleague() == null) {
                return pastCaseDTO;
            }
            pastCaseDTO.setAssignedColleagueEmail(caseEntity.getColleague().getEmail());
            pastCaseDTO.setAssignedColleagueFirstName(caseEntity.getColleague().getFirstName());
            pastCaseDTO.setAssignedColleagueLastName(caseEntity.getColleague().getLastName());

            return pastCaseDTO;
        }).collect(Collectors.toList());
    }

}

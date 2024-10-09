package com.calypso.binar.service;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.Reservation;
import com.calypso.binar.model.Status;
import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.CaseListDTO;
import com.calypso.binar.model.dto.PastCaseDTO;
import com.calypso.binar.repository.CaseRepository;
import com.calypso.binar.service.casedetails.CaseService;
import com.calypso.binar.service.exception.CaseNotFoundException;
import com.calypso.binar.service.exception.StatusNonExistent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaseServiceTest {

    @Mock
    private CaseRepository caseRepository;

    @InjectMocks
    private CaseService caseService;

    @Mock
    private StatusService statusService;

    @Test
    public void getAllCases_returnsAllCases_whenFindingCasesSucceeds() {
        // Given
        Case case1 = new Case();
        Case case2 = new Case();
        when(caseRepository.findAll()).thenReturn(Arrays.asList(case1, case2));

        // When
        List<Case> result = caseService.getAllCases();

        // Then
        assertEquals(2, result.size());
        verify(caseRepository, times(1)).findAll();
    }

    @Test
    public void getAllCases_returnsAllCases_whenCasesExist() {
        // Given
        Case case1 = new Case();
        case1.setCaseId(1);
        Reservation reservation1 = new Reservation();
        reservation1.setReservationNumber("ABC123");
        Status status1 = new Status();
        status1.setStatusName("NEW");
        case1.setReservation(reservation1);
        case1.setStatus(status1);

        Case case2 = new Case();
        case2.setCaseId(2);
        Reservation reservation2 = new Reservation();
        reservation2.setReservationNumber("DEF456");
        Status status2 = new Status();
        status2.setStatusName("ASSIGNED");
        case2.setReservation(reservation2);
        case2.setStatus(status2);

        when(caseRepository.findAll()).thenReturn(Arrays.asList(case1, case2));

        // When
        List<CaseListDTO> result = caseService.getAllCasesForAdmin();

        // Then
        assertEquals(2, result.size());
        assertEquals("ABC123", result.get(0).getReservationNumber());
        assertEquals("NEW", result.get(0).getStatusName());
        assertEquals("DEF456", result.get(1).getReservationNumber());
        assertEquals("ASSIGNED", result.get(1).getStatusName());
        verify(caseRepository, times(1)).findAll();
    }


    @Test
    public void getAllCases_returnExistingCase_whenOneIsMissing() {
        // Given
        Case case1 = new Case();
        case1.setCaseId(1);
        Reservation reservation1 = new Reservation();
        reservation1.setReservationNumber("ABC123");
        Status status1 = new Status();
        status1.setStatusName("NEW");
        case1.setReservation(reservation1);
        case1.setStatus(status1);

        Case case2 = new Case();
        case2.setCaseId(2);
        Reservation reservation2 = new Reservation();
        reservation2.setReservationNumber("DEF456");
        Status status2 = new Status();
        status2.setStatusName("ASSIGNED");
        case2.setReservation(reservation2);
        case2.setStatus(status2);

        when(caseRepository.findAll()).thenReturn(Arrays.asList(case1, case2));

        // When
        List<CaseListDTO> result = caseService.getAllCasesForAdmin();

        // Then
        assertEquals(2, result.size()); // All cases should be returned
        assertEquals("ABC123", result.get(0).getReservationNumber());
        assertEquals("NEW", result.get(0).getStatusName());
        assertEquals("DEF456", result.get(1).getReservationNumber());
        assertEquals("ASSIGNED", result.get(1).getStatusName());
        verify(caseRepository, times(1)).findAll();
    }

    @Test
    public void deleteCaseById_returnNoContent_whenCaseExists() throws CaseNotFoundException {
        // Given
        Integer caseId = 1;
        Case case1 = new Case();
        case1.setCaseId(caseId);
        when(caseRepository.findById(caseId)).thenReturn(Optional.of(case1));

        // When
        caseService.deleteCaseById(caseId);

        // Then
        verify(caseRepository, times(1)).deleteById(caseId);
    }


    @Test
    public void deleteCaseById_throwsCaseNotFoundException_whenCaseDoesNotExist() {
        // Given
        Integer caseId = 1;
        when(caseRepository.findById(caseId)).thenReturn(Optional.empty());

        // When & Then
        CaseNotFoundException exception = assertThrows(CaseNotFoundException.class, () -> {
            caseService.deleteCaseById(caseId);
        });

        assertEquals("Case with id " + caseId + " does not exist", exception.getMessage());
        verify(caseRepository, times(0)).deleteById(caseId);
    }

    @Test
    public void deleteCaseById_throwIllegalArgumentException_whenIdIsNegative() {

        // Given
        Integer caseId = -1;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            caseService.deleteCaseById(caseId);
        });

        assertEquals("Case id cannot be null or negative", exception.getMessage());
        verify(caseRepository, times(0)).deleteById(caseId);
    }

    @Test
    public void getPastCasesPassenger_returnsPastCaseDTOs_whenCasesExist() throws StatusNonExistent {
        // Given
        int passengerId = 1;
        Status invalidStatus = new Status("INVALID");
        Status validStatus = new Status("VALID");
        Case case1 = new Case();
        case1.setCaseId(1);
        case1.setSystemCaseId("SYS001");
        case1.setReservation(new Reservation());
        case1.setPassenger(new User());
        case1.setStatus(new Status());

        Case case2 = new Case();
        case2.setCaseId(2);
        case2.setSystemCaseId("SYS002");
        case2.setReservation(new Reservation());
        case2.setPassenger(new User());
        case2.setStatus(new Status());

        List<Case> cases = Arrays.asList(case1, case2);

        // Mocking the statusService and caseRepository
        when(statusService.getStatusByStatusName("INVALID")).thenReturn(invalidStatus);
        when(statusService.getStatusByStatusName("VALID")).thenReturn(validStatus);
        when(caseRepository.findPastCasesByPassengerId(passengerId, Arrays.asList("INVALID", "VALID")))
                .thenReturn(Optional.of(cases));

        // When
        List<PastCaseDTO> result = caseService.getPastCasesPassenger(passengerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getSystemCaseId()).isEqualTo("SYS001");
        assertThat(result.get(1).getSystemCaseId()).isEqualTo("SYS002");

        // Verify that the repository method was called
        verify(caseRepository, times(1))
                .findPastCasesByPassengerId(passengerId, Arrays.asList("INVALID", "VALID"));
    }

    @Test
    public void getPastCasesPassenger_returnsEmptyList_whenNoCasesExist() throws StatusNonExistent {
        // Given
        int passengerId = 1;
        Status invalidStatus = new Status("INVALID");
        Status validStatus = new Status("VALID");

        // Mocking the statusService and caseRepository
        when(statusService.getStatusByStatusName("INVALID")).thenReturn(invalidStatus);
        when(statusService.getStatusByStatusName("VALID")).thenReturn(validStatus);
        when(caseRepository.findPastCasesByPassengerId(passengerId, Arrays.asList("INVALID", "VALID")))
                .thenReturn(Optional.empty());

        // When
        List<PastCaseDTO> result = caseService.getPastCasesPassenger(passengerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);

        // Verify that the repository method was called
        verify(caseRepository, times(1))
                .findPastCasesByPassengerId(ArgumentMatchers.eq(passengerId), ArgumentMatchers.eq(Arrays.asList("INVALID", "VALID")));
    }


}

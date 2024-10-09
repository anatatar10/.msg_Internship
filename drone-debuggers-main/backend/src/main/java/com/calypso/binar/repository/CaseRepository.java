package com.calypso.binar.repository;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;



@Transactional
@Repository
public interface CaseRepository extends JpaRepository<Case,Integer> {
    @Query("SELECT c FROM Case c WHERE c.passenger.userId = :passengerId AND c.status.statusName IN (:statuses)")
    Optional<List<Case>> findPastCasesByPassengerId(@Param("passengerId") Integer passengerId, @Param("statuses") List<String> statuses);

    @Query("SELECT c FROM Case c LEFT JOIN FETCH c.reservation WHERE c.caseId = :caseId")
    Optional<Case> findByIdWithReservation(@Param("caseId") Integer caseId);

    Optional<List<Case>> findCasesByColleague(User colleague);


    Optional<Case> findBySystemCaseId(String systemCaseId);

    @Query("SELECT c FROM Case c WHERE c.passenger.userId = :passengerId AND c.status.statusName NOT IN (:statuses)")
    Optional<List<Case>> findActiveCasesByPassengerId(@Param("passengerId") int id, @Param("statuses") List<String> list);
}




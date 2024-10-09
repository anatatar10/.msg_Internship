package com.calypso.binar.repository;

import com.calypso.binar.model.PassengerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerDetailsRepository extends JpaRepository<PassengerDetails, Integer> {
    @Query("SELECT pd FROM PassengerDetails pd " +
            "JOIN Case cc ON pd.passengerDetailsId = cc.passengerDetails.passengerDetailsId " +
            "WHERE cc.systemCaseId = :systemCaseId")
    Optional<PassengerDetails> findByCaseId(@Param("systemCaseId") String systemCaseId);
}

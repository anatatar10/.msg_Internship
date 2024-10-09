package com.calypso.binar.repository;

import com.calypso.binar.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    @Query("SELECT d FROM Document d " +
            "JOIN Case cc ON d.caseEntity.caseId = cc.caseId " +
            "WHERE cc.systemCaseId = :systemCaseId")
    Optional<List<Document>> findAllByCaseId(@Param("systemCaseId") String systemCaseId);
}

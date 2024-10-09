package com.calypso.binar.repository;

import com.calypso.binar.model.CancellationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CancellationTypeRepository extends JpaRepository<CancellationType, Integer> {
    Optional<CancellationType> findByCancellationTypeDescription(String description);
}

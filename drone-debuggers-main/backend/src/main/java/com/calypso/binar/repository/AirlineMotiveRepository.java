package com.calypso.binar.repository;

import com.calypso.binar.model.AirlineMotive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirlineMotiveRepository extends JpaRepository<AirlineMotive, Integer> {
    Optional<AirlineMotive> findByAirlineMotiveTypeDescription(String description);
}

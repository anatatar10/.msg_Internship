package com.calypso.binar.repository;

import com.calypso.binar.model.Disruption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisruptionRepository extends JpaRepository<Disruption, Integer> {
}

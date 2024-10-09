package com.calypso.binar.repository;

import com.calypso.binar.model.DisruptionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisruptionOptionRepository extends JpaRepository<DisruptionOption, Integer> {
    Optional<DisruptionOption> findByDisruptionOptionDescription(String description);
}

package com.calypso.binar.repository;

import com.calypso.binar.model.GeneratedPdf;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
public interface GeneratedPdfRepository extends JpaRepository<GeneratedPdf, Integer> {
}

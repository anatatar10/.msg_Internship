package com.calypso.binar.repository;

import com.calypso.binar.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {
    Optional<DocumentType> findDocumentTypeByDocumentTypeName(String documentType);
}

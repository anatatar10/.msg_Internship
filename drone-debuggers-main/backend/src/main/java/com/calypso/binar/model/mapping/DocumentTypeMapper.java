package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.DocumentTypeDTO;
import com.calypso.binar.model.DocumentType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentTypeMapper {

    DocumentTypeDTO toDocumentTypeDTO(DocumentType documentType);

    DocumentType toDocumentTypeEntity(DocumentTypeDTO documentTypeDTO);
}

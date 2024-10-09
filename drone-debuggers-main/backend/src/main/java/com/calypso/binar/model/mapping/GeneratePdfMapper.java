package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.GeneratedPdfDTO;
import com.calypso.binar.model.GeneratedPdf;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CaseMapper.class})
public interface GeneratePdfMapper {
    GeneratedPdfDTO toGeneratedPdfDTO(GeneratedPdf generatedPdf);

    GeneratedPdf toGeneratedPdfEntity(GeneratedPdfDTO generatedPdfDTO);
}

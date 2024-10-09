package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.CancellationTypeDTO;
import com.calypso.binar.model.CancellationType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CancellationTypeMapper {
    CancellationTypeDTO toCancellationTypeDTO(CancellationType cancellationType);

    CancellationType toCancellationTypeEntity(CancellationTypeDTO cancellationTypeDTO);
}

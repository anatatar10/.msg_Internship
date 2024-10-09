package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.StatusDTO;
import com.calypso.binar.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatusMapper {

    @Mapping(source = "statusId", target = "statusID")
    StatusDTO toStatusDTO(Status status);

    @Mapping(source = "statusID", target = "statusId")
    Status toStatusEntity(StatusDTO statusDTO);

}

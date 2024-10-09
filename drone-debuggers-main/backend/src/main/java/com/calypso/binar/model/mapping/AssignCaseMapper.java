package com.calypso.binar.model.mapping;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.User;
import com.calypso.binar.model.dto.AssignCaseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssignCaseMapper {

    AssignCaseDTO toAssignCaseDto(Case caseEntity, User assignedColleague);

}

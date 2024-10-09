package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.CaseDTO;
import com.calypso.binar.model.Case;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StatusMapper.class, UserMapper.class, ReservationMapper.class, PassengerDetailsMapper.class, DisruptionMapper.class})
public interface CaseMapper {

    @Mapping(source = "colleague", target = "colleague")
    @Mapping(source = "passenger", target = "passenger")
    @Mapping(source = "caseId", target = "caseId")
    @Mapping(source = "reservation", target = "reservationData")
    CaseDTO toCaseDTO(Case aCase);

    Case toCaseEntity(CaseDTO caseDTO);



}

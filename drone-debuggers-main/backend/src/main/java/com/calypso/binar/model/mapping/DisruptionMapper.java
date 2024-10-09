package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.DisruptionDTO;
import com.calypso.binar.model.Disruption;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CancellationTypeMapper.class, DisruptionOptionMapper.class, AirlineMotiveMapper.class})
public interface DisruptionMapper {

    DisruptionDTO toDisruptionDTO(Disruption disruption);

    Disruption toDisruptionEntity(DisruptionDTO disruptionDTO);
}
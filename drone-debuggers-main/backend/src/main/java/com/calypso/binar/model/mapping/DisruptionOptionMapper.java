package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.DisruptionOptionDTO;
import com.calypso.binar.model.DisruptionOption;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DisruptionOptionMapper {


    DisruptionOptionDTO toDisruptionOptionDTO(DisruptionOption disruptionOption);

    DisruptionOption toDisruptionOptionEntity(DisruptionOptionDTO disruptionOptionDTO);
}

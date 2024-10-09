package com.calypso.binar.model.mapping;


import com.calypso.binar.model.dto.AirlineMotiveDTO;
import com.calypso.binar.model.AirlineMotive;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AirlineMotiveMapper {

    AirlineMotiveDTO toAirlineMotiveDTO(AirlineMotive airlineMotive);

    AirlineMotive toAirlineMotiveEntity(AirlineMotiveDTO airlineMotiveDTO);
}

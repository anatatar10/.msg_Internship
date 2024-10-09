package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.FlightInfoDTO;
import com.calypso.binar.model.FlightInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FlightInfoMapper {

    @Mapping(source = "flightNr", target = "flightNumber")
    FlightInfoDTO toFlightInfoDTO(FlightInfo flightInfo);

    @Mapping(source = "flightNumber", target = "flightNr")
    @Mapping(source = "departureAirport", target = "departingAirport")
    @Mapping(source = "arrivalAirport", target = "destinationAirport")
    @Mapping(source = "departureDate", target = "plannedDepartureDate")
    @Mapping(source = "arrivalDate", target = "plannedArrivalDate")
    @Mapping(source = "problematic", target = "problemFlight")
    FlightInfo toFlightInfoEntity(FlightInfoDTO flightInfoDTO);
}

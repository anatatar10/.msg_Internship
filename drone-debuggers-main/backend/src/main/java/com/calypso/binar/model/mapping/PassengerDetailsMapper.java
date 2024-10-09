package com.calypso.binar.model.mapping;

import com.calypso.binar.model.dto.PassengerDetailsDTO;
import com.calypso.binar.model.PassengerDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface PassengerDetailsMapper {


    @Mapping(source="phone", target="phoneNumber")
    PassengerDetailsDTO toPassengerDetailsDTO(PassengerDetails passengerDetails);

    PassengerDetails toPassengerDetailsEntity(PassengerDetailsDTO passengerDetailsDTO);
}

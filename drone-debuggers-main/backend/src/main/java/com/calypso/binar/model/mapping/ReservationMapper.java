package com.calypso.binar.model.mapping;

import com.calypso.binar.model.Reservation;
import com.calypso.binar.model.dto.ReservationSaveCaseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")

public interface ReservationMapper {

    ReservationSaveCaseDTO toReservationDTO(Reservation reservation);

    @Mapping(source = "departingAirport", target = "departingAirport")
    @Mapping(source = "arrivingAirport", target = "destinationAirport")
    @Mapping(source = "reservationNumber", target = "reservationNumber")
    Reservation toReservationEntity(ReservationSaveCaseDTO reservationDTO);
}

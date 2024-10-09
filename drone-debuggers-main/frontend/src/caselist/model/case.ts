export interface Case {
  dateCreated: string;
  status: StatusDTO;
  reservationData: ReservationDTO;
  passengerDetails: PassengerDetailsDTO;
  disruption: DisruptionDTO;
  passenger: UserDTO;
  colleague: ColleagueDTO;
  flightsInfo: FlightInfoDTO[];
  systemCaseId: string;
}

export interface StatusDTO {
  statusName: string;
}

export interface ReservationDTO {
  reservationNumber: string;
}

export interface PassengerDetailsDTO {
  firstName: string;
  lastName: string;
}

export interface DisruptionDTO {
  disruptionType: string;
}

export interface UserDTO {
  email: string;
}

export interface ColleagueDTO {
  email: string;
  firstName: string;
  lastName: string;
}



export interface FlightInfoDTO {
  systemCaseId: string;
  dateCreated: Date;
  flightNumber: string;
}

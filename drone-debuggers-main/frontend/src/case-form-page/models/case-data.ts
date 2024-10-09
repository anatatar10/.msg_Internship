import {ReservationData} from "./reservation-data";
import {PassengerData} from "./passenger-data";
import {DisruptionInfo} from "./disruption-info";
import {UserData} from "./user-data";
import {FlightInfo} from "./flight-info";
import {Status} from "./enums/status.enum";

export class CaseData {
  dateCreated: Date;
  status: string;
  reservationData: ReservationData;
  passengerDetails: PassengerData;
  disruption: DisruptionInfo;
  passenger: UserData;
  flightsInfo: FlightInfo[];
  systemCaseId: string;

  constructor(status: string = Status.INCOMPLETE, reservation: ReservationData, passengerDetails: PassengerData, disruption: DisruptionInfo, passenger: UserData, flightsInfo: FlightInfo[], systemCaseId: string) {
    this.dateCreated = new Date();
    this.status = status;
    this.reservationData = reservation;
    this.passengerDetails = passengerDetails;
    this.disruption = disruption;
    this.passenger = passenger;
    this.flightsInfo = flightsInfo;
    this.systemCaseId = systemCaseId
  }

}

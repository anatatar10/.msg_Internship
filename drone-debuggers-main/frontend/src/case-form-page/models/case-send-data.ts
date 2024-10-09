import {ReservationData} from "./reservation-data";
import {PassengerData} from "./passenger-data";
import {DisruptionInfo} from "./disruption-info";
import {UserData} from "./user-data";
import {FlightInfo} from "./flight-info";
import {Status} from "./enums/status.enum";
import {PassengerSendData} from "./passenger-send-data";

export class CaseSendData {
  dateCreated: Date;
  status: string;
  reservationData: ReservationData;
  passengerDetails: PassengerSendData;
  disruption: DisruptionInfo;
  passenger: UserData;
  flightsInfo: FlightInfo[];
  systemCaseId: string;

  constructor(status: string = Status.INCOMPLETE, reservation: ReservationData, passengerDetails: PassengerSendData, disruption: DisruptionInfo, passenger: UserData, flightsInfo: FlightInfo[], systemCaseId: string) {
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

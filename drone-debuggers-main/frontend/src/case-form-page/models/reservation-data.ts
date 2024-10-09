import {Airport} from "./airport";

export interface ReservationData {
  reservationNumber: string;
  departingAirport: Airport;
  arrivingAirport: Airport;
  stops: Airport[];
  possibleCompensation: number;
}

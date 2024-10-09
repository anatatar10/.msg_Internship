import {Flight} from "./flight";
import {Airport} from "../../../case-form-page/models/airport";

export interface Reservation {
  reservationNumber: string;
  departingAirport: Airport;
  destinationAirport: Airport;
}

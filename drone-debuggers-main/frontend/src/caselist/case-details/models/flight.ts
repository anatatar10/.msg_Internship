import {Airport} from "../../../case-form-page/models/airport";

export interface Flight {
  flightNumber: string;
  airline: string;
  departureAirport: Airport;
  arrivalAirport: Airport;
  departureDate: Date;
  arrivalDate: Date;
  problematic: boolean;
}


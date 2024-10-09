import {Airport} from "./airport";

export class FlightInfo {
  flightNumber: string;
  departureAirport: Airport;
  arrivalAirport: Airport;
  departureDate: Date;
  arrivalDate: Date;
  airline: string;
  isProblematic: boolean;

  constructor(
    flightNumber: string,
    departureAirport: Airport,
    arrivalAirport: Airport,
    departureDate: Date,
    arrivalDate: Date,
    airline: string,
    isProblematic: boolean = false // Default value here
  ) {
    this.flightNumber = flightNumber;
    this.departureAirport = departureAirport;
    this.arrivalAirport = arrivalAirport;
    this.departureDate = departureDate;
    this.arrivalDate = arrivalDate;
    this.airline = airline;
    this.isProblematic = isProblematic;
  }
}

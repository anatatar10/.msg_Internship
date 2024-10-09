import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {FlightInfo} from '../models/flight-info';
import {Airport} from '../models/airport';

@Injectable({
  providedIn: 'root'
})
export class FlightDataService {
  private flightsSubject = new BehaviorSubject<FlightInfo[]>([
    {
      flightNumber: 'FL123',
      departureAirport: {airportCode: 'JFK', airportName: 'John F. Kennedy International Airport'},
      arrivalAirport: {airportCode: 'LAX', airportName: 'Los Angeles International Airport'},
      departureDate: new Date('2024-08-15T08:00:00'),
      arrivalDate: new Date('2024-08-15T11:00:00'),
      airline: 'Delta Airlines',
      isProblematic: false
    },
    {
      flightNumber: 'FL456',
      departureAirport: {airportCode: 'LAX', airportName: 'Los Angeles International Airport'},
      arrivalAirport: {airportCode: 'DFW', airportName: 'Dallas/Fort Worth International Airport'},
      departureDate: new Date('2024-08-15T14:00:00'),
      arrivalDate: new Date('2024-08-15T16:00:00'),
      airline: 'American Airlines',
      isProblematic: false
    }
  ]);

  constructor() {
  }

  public setFlights(flights: FlightInfo[]) {
    this.flightsSubject.next(flights);
  }

  public getFlights() {
    return this.flightsSubject.getValue();
  }
}

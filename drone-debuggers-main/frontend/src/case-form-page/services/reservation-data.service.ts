import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {ReservationData} from "../models/reservation-data";
import {Airport} from "../models/airport";

@Injectable({
  providedIn: 'root'
})
export class ReservationDataService {
  private reservationDataSubject = new BehaviorSubject<ReservationData>({
    arrivingAirport: {airportCode: "LHR", airportName: "airport4"},
    departingAirport: {airportCode: "CDG", airportName: "airport1"},
    reservationNumber: "123",
    // stops: [{airportCode:"a2", airportName:"airport2"},{airportCode:"a3", airportName:"airport3"}],
    stops: [],
    possibleCompensation: 250
  });


  public setReservationData(data: ReservationData) {
    this.reservationDataSubject.next(data);
  }

  public getReservationData() {
    return this.reservationDataSubject.getValue();
  }
}

import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {PassengerData} from '../models/passenger-data'; // Adjust the import path as needed

@Injectable({
  providedIn: 'root'
})
export class PassengerDataService {
  private passengerDataSubject = new BehaviorSubject<PassengerData>({
    dateOfBirth: new Date(),
    phoneNumber: '1234567890',
    address: 'street x',
    postalCode: '123',
    boardingPass: new File([], "boardingPass"),
    idPassport: new File([], "idPassport")
  });

  constructor() {
  }

  // Get the current passenger data as an Observable
  public getPassengerData() {
    return this.passengerDataSubject.getValue();
  }

  // Set the passenger data
  public setPassengerData(passengerData: PassengerData): void {
    this.passengerDataSubject.next(passengerData);
  }

}

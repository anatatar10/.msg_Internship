import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Reservation} from "../models/reservation";
import {Passenger} from "../models/passenger";

@Injectable({
  providedIn: 'root'
})
export class PassengerDetailsService {

  constructor(private http: HttpClient) {}

    getPassengerDetailsByCaseId(caseId: string | null | undefined) {
    return this.http.get<Passenger>(`/api/passenger-details/case/${caseId}`);
  }
}

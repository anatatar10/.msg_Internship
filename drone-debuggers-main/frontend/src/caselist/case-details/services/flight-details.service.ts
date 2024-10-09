import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Reservation} from "../models/reservation";
import {Flight} from "../models/flight";

@Injectable({
  providedIn: 'root'
})
export class FlightDetailsService {

  constructor(private http: HttpClient) {}

  getFlightsByCaseId(caseId: string | null | undefined) {
    return this.http.get<Flight[]>(`/api/flight/case/${caseId}`);
  }
}

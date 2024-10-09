import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Reservation} from "../models/reservation";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  constructor(private http: HttpClient) {}

  getReservationByCaseId(caseId: string | null | undefined) {
    return this.http.get<Reservation>(`/api/reservation/case/${caseId}`);
  }
}

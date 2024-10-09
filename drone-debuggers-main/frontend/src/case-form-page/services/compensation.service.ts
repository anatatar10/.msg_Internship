import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CompensationRequest} from "../models/compensation-request";

@Injectable({
  providedIn: 'root'
})
export class CompensationService {

  private apiUrl = '/api/compensation/calculate';

  constructor(private http: HttpClient) {
  }

  // Method to make a POST request
  public getPossibleCompensation(from: string, to: string): Observable<number> {
    return this.http.post<number>(this.apiUrl, {from, to} as CompensationRequest);
  }
}

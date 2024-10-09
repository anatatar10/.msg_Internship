import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ColleagueData } from '../models/colleague-details'; // Adjust the import path as needed
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ColleagueDataService {
  private colleagueDataSubject = new BehaviorSubject<ColleagueData>(
    {
      firstName: 'John',
      lastName: 'Doe',
      email: 'a@a.a',
      password: 'lalalalala' // Assuming password is an empty string initially
    } as ColleagueData
  );
  private userApiUrl = '/api/user/create-colleague'; // Ensure this endpoint is appropriate for the new data

  constructor(private http: HttpClient) { }

  // Get the current colleague data as an Observable
  public getColleagueData(): ColleagueData {
    return this.colleagueDataSubject.getValue();
  }

  // Set the colleague data
  public setColleagueData(colleagueData: ColleagueData): void {
    this.colleagueDataSubject.next(colleagueData);
  }

  public createUser(colleagueData: ColleagueData): Observable<string> {
    return this.http.post<string>(this.userApiUrl, colleagueData);
  }
}

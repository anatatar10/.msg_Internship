import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {UserData} from '../models/user-data';
import {HttpClient} from "@angular/common/http";
import {CompensationRequest} from "../models/compensation-request"; // Adjust the import path as needed

@Injectable({
  providedIn: 'root'
})
export class UserDataService {
  private userDataSubject = new BehaviorSubject<UserData>(
    {
      firstName: 'John',
      lastName: 'Doe',
      email: 'a@a.a'
    } as UserData);
  private userApiUrl = '/api/user/create-passenger';

  constructor(private http: HttpClient) {
  }

  // Get the current user data as an Observable
  public getUserData() {
    return this.userDataSubject.getValue();
  }

  // Set the user data
  public setUserData(userData: UserData): void {
    this.userDataSubject.next(userData);
  }

  public createUser(userData: UserData): Observable<string>  {
    return this.http.post<string>(this.userApiUrl, userData);

  }


}

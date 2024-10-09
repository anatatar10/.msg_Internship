import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserListDTO} from "../model/userListDTO";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) { }

  getUsersFromDB(): Observable<UserListDTO[]>{
    return this.http.get<UserListDTO[]>('/api/user/show-all-passenger-colleague');
  }

  deleteUser(email: string){
    return this.http.delete(`/api/user/delete-user/${email}`);
  }
}

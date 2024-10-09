import {HttpClient, HttpHeaders} from "@angular/common/http";
import {LoginData} from "../models/loginData";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor (private http:HttpClient) {
  }

  loginAttempt(loginEntity:LoginData){
    return this.http.post('/api/auth/login', loginEntity).pipe();
  }

  //deletes the jwt token from be
  logOut(token: string | null){
    let jsonWithToken = {token};
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post('/api/auth/logout', jsonWithToken, { headers }).pipe();
  }

  changePassword(password:string, email:string | null){
    const body = { email, password };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post('/api/auth/change-password', body, { headers }).pipe();
  }

}

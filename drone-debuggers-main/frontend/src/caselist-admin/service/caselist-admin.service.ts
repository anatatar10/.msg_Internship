import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {CaseListDTO} from "../model/caseListDTO";

@Injectable({
  providedIn: 'root'
})
export class CaselistAdminService {

  constructor(private http: HttpClient) { }

  getCasesFromDB(): Observable<CaseListDTO[]>{
    return this.http.get<CaseListDTO[]>('/api/case/show-list');
  }

  deleteCase(caseId: number): Observable<void> {
    return this.http.delete<void>(`/api/case/${caseId}`);
  }
}

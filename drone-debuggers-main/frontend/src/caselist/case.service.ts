import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Case, ColleagueDTO, UserDTO} from "./model/case";
import {HttpClient} from "@angular/common/http";
import {CaseSummary} from "./model/caseSummary";
import {AttachedDocument} from "./case-details/models/attachedDocument";
import {Attachment} from "../case-form-page/models/attachment";
import {PastCase} from "./model/pastCase";

@Injectable({
  providedIn: 'root'
})
export class CaseService {

  private uploadFilesApiUrl = '/api/case/uploadCaseFiles';

  private apiUrl = '/api/case';

  constructor(private http: HttpClient) { }

  getCases(): Observable<CaseSummary[]> {
    return this.http.get<CaseSummary[]>('/api/case/summaries');
  }

  getColleagues(): Observable<ColleagueDTO[]> {
    return this.http.get<ColleagueDTO[]>('/api/case/colleagues');
  }

  updateCaseStatus(systemCaseId: String, status: string): Observable<void> {
    const url = `${this.apiUrl}/${systemCaseId}/status`;
    return this.http.post<void>(url, { status });
  }

  assignColleague(systemCaseId: string, assignedColleagueEmail: string): Observable<void> {
    const body = { systemCaseId, assignedColleagueEmail };
    return this.http.post<void>('/api/case/assign', body);
  }

  getPastCases(email:string): Observable<PastCase[]>{
    const url = "/api/case/past-cases/"
    return this.http.get<PastCase[]>(url+email);
  }

  getActiveCases(email:string): Observable<PastCase[]>{
    const url = "/api/case/active-cases/"
    return this.http.get<PastCase[]>(url+email);
  }


  sendCaseFiles(documents: Attachment[], systemCaseId:string): Observable<any> {
    return this.http.post<string>(this.uploadFilesApiUrl+"/"+systemCaseId, documents);
  }


}


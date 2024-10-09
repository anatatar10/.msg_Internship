import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PdfDownloadService {


  constructor(private http: HttpClient) { }

  downloadPdf(caseId: number): Observable<any> {
    const url = `/api/pdf/${caseId}/generate`;
    return this.http.get(url, { observe: 'response', responseType: 'blob' });
  }
}

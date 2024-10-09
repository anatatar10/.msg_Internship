import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Reservation} from "../models/reservation";
import {AttachedDocument} from "../models/attachedDocument";
import {catchError} from "rxjs/operators";
import {of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AttachedDocumentListService {

  constructor(private http: HttpClient) {}

  getDocumentsByCaseId(caseId: string | null | undefined) {
    return this.http.get<AttachedDocument[]>(`/api/document/case/${caseId}`).pipe(
      catchError(error => {
        console.error('Error fetching documents:', error);
        return of([]); // Return an empty array if an error occurs
      })
    );  }

  public getDocumentById(documentId: number)  {
    const documentUrl = `/api/document/download/${documentId}`;
    return this.http.get(documentUrl, { responseType: 'arraybuffer' })
  }
}

import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {GeneratedPdf} from "../model/generatedPdf";

@Injectable({
  providedIn: 'root'
})
export class PdfListService {
  private getPdfUrl: string = "/api/pdf/all"

  constructor(private http: HttpClient) { }

  getPdfList() {
    return this.http.get<GeneratedPdf[]>(this.getPdfUrl);
  }
}

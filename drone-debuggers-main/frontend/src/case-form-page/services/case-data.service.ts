import {Injectable, OnDestroy} from '@angular/core';
import {BehaviorSubject, Observable, of, Subscription} from 'rxjs';
import {ReservationDataService} from './reservation-data.service';
import {PassengerDataService} from './passenger-data.service';
import {DisruptionDataService} from './disruption-data.service';
import {UserDataService} from './user-data.service';
import {FlightDataService} from './flight-data.service';
import {CaseData} from '../models/case-data';
import {PassengerData} from '../models/passenger-data';
import {FlightInfo} from "../models/flight-info";
import {Status} from "../models/enums/status.enum";
import {HttpClient} from "@angular/common/http";
import {CaseSendData} from "../models/case-send-data";
import {PassengerSendData} from "../models/passenger-send-data";
import {AttachmentSend} from "./attachment-send";
import {Attachment} from "../models/attachment";

@Injectable({
  providedIn: 'root'
})
export class CaseDataService {
  private caseDataSubject = new BehaviorSubject<CaseData>({} as CaseData);

  private caseApiUrl = '/api/case/save';
  private uploadFilesApiUrl = '/api/case/uploadCaseFiles';
  private sendPdfApiUrl: string = '/api/case/sendPdf';

  constructor(
    private reservationDataService: ReservationDataService,
    private passengerDataService: PassengerDataService,
    private disruptionDataService: DisruptionDataService,
    private userDataService: UserDataService,
    private flightDataService: FlightDataService,
    private http: HttpClient,

  ) {
  }

  public getCaseData() {
    return this.caseDataSubject.getValue();
  }

  // Method to create an incomplete case
  public createIncompleteCase(): void {

    const reservationData = this.reservationDataService.getReservationData();
    const disruptionData = this.disruptionDataService.getDisruptionInfo();
    const userData = this.userDataService.getUserData();
    const flightsInfo: FlightInfo[] = this.flightDataService.getFlights();
    const systemCaseId = ""

    //Incomplete case se trimite in back fara systemCase id, raspuns la salvare ii systemCaseId
    const caseData = new CaseData(
      Status.INCOMPLETE,
      reservationData,
      {} as PassengerData,
      disruptionData,
      userData,
      flightsInfo,
      systemCaseId
    );

    this.setCaseData(caseData);
  }

// Method to create a complete case
  public createCompleteCase(): void {
    let existingCaseData = this.getCaseData();
    const passengerData = this.passengerDataService.getPassengerData();
    const newCase = new CaseData(
      Status.NEW,
      existingCaseData.reservationData,
      passengerData,
      existingCaseData.disruption,
      existingCaseData.passenger,
      existingCaseData.flightsInfo,
      existingCaseData.systemCaseId
    );
    this.setCaseData(newCase);


  }

  public updateSystemCaseId(systemCaseId: string): void {
    let existingCaseData = this.caseDataSubject.value;

    existingCaseData.systemCaseId = systemCaseId;
    this.setCaseData(existingCaseData);
  }

  private setCaseData(caseData: CaseData): void {
    this.caseDataSubject.next(caseData);
  }

  public sendCase(): Observable<string>{
    return this.http.post<string>(this.caseApiUrl, this.caseDataSubject.value, {responseType: 'text' as 'json'});
  }

  public sendCaseFiles(attachments: Attachment[]): Observable<any> {
    const formData = new FormData();
    const existingCaseData = this.caseDataSubject.value;
    const systemCaseId = existingCaseData.systemCaseId;


    // Send the FormData to the backend
    return this.http.post<string>(this.uploadFilesApiUrl+"/"+systemCaseId, attachments);
  }


  sendPdf() {
    return this.http.get<string>(this.sendPdfApiUrl+"/"+this.caseDataSubject.value.systemCaseId)

  }
}

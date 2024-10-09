// src/app/services/disruption-data.service.ts

import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {DisruptionInfo} from '../models/disruption-info';

@Injectable({
  providedIn: 'root'
})
export class DisruptionDataService {
  private disruptionInfoSubject = new BehaviorSubject<DisruptionInfo>({
    cancellationType: '',
    disruptionOption: '',
    airlineMotive: '',
    incidentDescription: ''
  });

  // Get current disruption info
  public getDisruptionInfo() {
    return this.disruptionInfoSubject.getValue();
  }

  // Set disruption info
  public setDisruptionInfo(disruptionInfo: DisruptionInfo) {
    this.disruptionInfoSubject.next(disruptionInfo);
  }
}

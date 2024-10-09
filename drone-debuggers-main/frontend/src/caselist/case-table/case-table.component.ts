import {UserDTO} from '../model/case';
import {Component, OnInit} from '@angular/core';
import {CaseService} from '../case.service';
import {firstValueFrom, take} from 'rxjs';
import {ActivatedRoute} from "@angular/router";
import {ConfirmationService, MessageService} from "primeng/api";
import {CaseSummary} from "../model/caseSummary";
import {TranslateService} from "@ngx-translate/core";

import {AuthInterceptor} from "../../shared/interceptors/auth.interceptor";
import {RolesEnum} from "../../case-form-page/models/enums/roles.enum";
import {PastCase} from "../model/pastCase";
import {Status} from "../../case-form-page/models/enums/status.enum";

@Component({
  selector: 'app-case-table',
  templateUrl: './case-table.component.html',
  styleUrls: ['./case-table.component.scss']
})
export class CaseTableComponent implements OnInit {

  cases: CaseSummary[] = [];
  userCases: PastCase[] = [];
  colleagues: UserDTO[] = [];
  colleaguesWithUnassigned: any[] = [];
  systemCaseId: string | undefined;
  emailIsNull: { [key: string]: boolean } = {};
  isAdmin: boolean = false;
  isPassenger: boolean = false;
  isColleague: boolean = false;
  email: string = '';
  selected: number | undefined;

  constructor(
    private caseService: CaseService,
    private confirmationService: ConfirmationService,
    private route: ActivatedRoute,
    private translate: TranslateService,
    private messageService: MessageService
  ) {
  }

  ngOnInit(): void {

    this.isAdmin = sessionStorage.getItem('role') === RolesEnum.SysAdmin;
    this.isPassenger = sessionStorage.getItem('role') === RolesEnum.Passenger;
    this.isColleague = sessionStorage.getItem('role') === RolesEnum.Colleague;

    this.email = sessionStorage.getItem('email') || '';




    this.systemCaseId = this.route.snapshot.paramMap.get('id') || undefined;

    if (this.isPassenger) {
      this.loadActiveCases();
    } else if (this.isColleague) {
      this.loadCases();
      this.loadColleagues();
    }

  }


  isColleagueNull(complainCase: CaseSummary): boolean {
    return !complainCase.assignedColleagueDTO;
  }

  isEmailNull(complainCase: CaseSummary): boolean {
    return this.emailIsNull[complainCase.systemCaseId];
  }

  isIncomplete(complainCase: CaseSummary): boolean {
    return complainCase.status === Status.INCOMPLETE;
  }

  confirmEligibility(systemCaseId: string, isEligible: boolean, event: Event): void {
    const newStatus = isEligible ? 'VALID' : 'INVALID';

    this.translate.get('caseList.ELIGIBILITY_CONFIRMATION', {status: newStatus.toLowerCase()})
      .pipe(take(1))
      .subscribe((translatedMessage: string) => {
        this.confirmationService.confirm({
          target: event.target as EventTarget,
          message: translatedMessage,
          header: this.translate.instant('caseList.CONFIRMATION'),
          icon: 'pi pi-exclamation-triangle',
          acceptButtonStyleClass: 'p-button-danger p-button-text',  // Customize styles as needed
          rejectButtonStyleClass: 'p-button-text',
          accept: () => {
            this.onEligibilityChange(systemCaseId, newStatus);
          },
          reject: () => {
            this.translate.get('caseList.TOAST_ELIGIBILITY_CANCELLED')
              .pipe(take(1))
              .subscribe((translatedCancelMessage: string) => {
                this.messageService.add({
                  severity: 'info',
                  summary: this.translate.instant('errors.error'),
                  detail: translatedCancelMessage,
                  life: 3000
                });
              });
          }
        });
      });
  }


  private async loadPastCases(): Promise<void> {
    try {
      this.selected=1;
      this.userCases = await firstValueFrom(this.caseService.getPastCases(this.email));
    } catch (error) {
      console.error('Error loading past cases:', error);
      throw error;
    }
  }

  private async loadActiveCases(): Promise<void> {
    try {
      this.selected=0;
      this.userCases = await firstValueFrom(this.caseService.getActiveCases(this.email));
    } catch (error) {
      console.error('Error loading past cases:', error);
      throw error;
    }
  }

  onActiveCasesClick(): void {
    this.loadActiveCases(); // Încarcă doar cazurile active
  }

  onPastCasesClick(): void {
    this.loadPastCases(); // Încarcă doar cazurile trecute
  }



  private async loadCases(): Promise<void> {
    try {
      this.cases = await firstValueFrom(this.caseService.getCases());
    } catch (error) {
      console.error('Error loading cases:', error);
      throw error;
    }
  }


  loadColleagues(): void {
    this.caseService.getColleagues().subscribe(data => {
      this.colleagues = data;

      const unassigned = {firstName: 'Unassigned', lastName: '', email: null};

      this.colleaguesWithUnassigned = [unassigned, ...this.colleagues];


      this.cases.forEach((complainCase => {
        complainCase.assignedColleagueDTO = complainCase.assignedColleagueDTO || null;
      }))
    });
  }

  async onColleagueChange(systemCaseId: string, colleague: any): Promise<void> {
    try {
      const email = colleague && colleague.email ? colleague.email : null;
      const body = {
        caseId: systemCaseId,
        assignedColleagueEmail: email
      };


      // Actualizează starea emailIsNull
      this.emailIsNull[systemCaseId] = email === null;

      await firstValueFrom(this.caseService.assignColleague(systemCaseId, email));

      const newStatus = email ? 'ASSIGNED' : 'NEW';
      await firstValueFrom(this.caseService.updateCaseStatus(systemCaseId, newStatus));

      this.updateCaseStatus(systemCaseId, newStatus);

      // Găsește cazul respectiv și actualizează assignedColleagueDTO
      const caseToUpdate = this.cases.find(c => c.systemCaseId === systemCaseId);
      if (caseToUpdate) {
        // Recreează obiectul pentru a forța detectarea schimbărilor
        caseToUpdate.assignedColleagueDTO = colleague ? { ...colleague } : null;
      }

    } catch (error) {
      console.error('Error assigning colleague:', error);
    }
  }


  async onEligibilityChange(systemCaseId: string, eligibilityStatus: string): Promise<void> {
    try {
      const body = {
        systemCaseId: systemCaseId,
        eligible: eligibilityStatus
      }
      await firstValueFrom(this.caseService.updateCaseStatus(systemCaseId, eligibilityStatus))
      this.updateCaseStatus(systemCaseId, eligibilityStatus);
    } catch (error) {
      console.error('Error assigning colleague', error);
    }
  }


  private updateCaseStatus(systemCaseId: string, status: string): void {
    const caseToUpdate = this.cases.find(c => c.systemCaseId === systemCaseId);
    if (caseToUpdate) {
      caseToUpdate.status = status;
    }
  }

  protected readonly Status = Status;
}

import { Component, OnInit } from '@angular/core';
import {firstValueFrom, take} from "rxjs";
import { CaseListDTO } from "../model/caseListDTO";
import { CaselistAdminService } from "../service/caselist-admin.service";
import { TranslateService } from "@ngx-translate/core";
import { MessageService, ConfirmationService } from "primeng/api";

@Component({
  selector: 'app-case-list-admin',
  templateUrl: './case-list-admin.component.html',
  styleUrl: './case-list-admin.component.scss',
  providers: [ConfirmationService, MessageService]
})
export class CaseListAdminComponent implements OnInit {
  cases: CaseListDTO[] = [];

  constructor(
    private caselistAdminService: CaselistAdminService,
    private translate: TranslateService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit(): void {
    this.loadCases();
  }

  private async loadCases(): Promise<void> {
    try {
      this.cases = await firstValueFrom(this.caselistAdminService.getCasesFromDB());
    } catch (error) {
      throw error;
    }
  }


confirmDelete(caseId: number, event: Event): void {
  this.translate.get('caseListForAdmin.DELETE_CONFIRMATION').pipe(take(1)).subscribe((translatedMessage: string) => {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: translatedMessage,
      header: this.translate.instant('caseListForAdmin.CONFIRMATION'),
      icon: 'pi pi-exclamation-triangle',
      acceptButtonStyleClass: 'p-button-danger p-button-text',
      rejectButtonStyleClass: 'p-button-text',
      accept: () => {
        this.deleteCase(caseId);
      },
      reject: () => {
        this.translate.get('caseListForAdmin.TOAST_DELETE_CANCELLED').pipe(take(1)).subscribe((translatedCancelMessage: string) => {
          this.messageService.add({
            severity: 'info',
            summary: this.translate.instant('userListDTO.CANCEL'),
            detail: translatedCancelMessage,
            life: 3000
          });
        });
      }
    });
  });
}


  private deleteCase(caseId: number): void {
    this.caselistAdminService.deleteCase(caseId).subscribe(() => {
      this.cases = this.cases.filter(caseItem => caseItem.caseListId !== caseId);
      this.loadCases();
      this.translate.get('caseListForAdmin.TOAST_DELETE_SUCCESSFUL').pipe(take(1)).subscribe((translatedSuccessMessage: string) => {
        this.messageService.add({
          severity: 'success',
          summary: this.translate.instant('user-details.success'),
          detail: translatedSuccessMessage,
          life: 4000
        });
      });
    }, error => {
      this.translate.get('caseListForAdmin.TOAST_DELETE_ERROR').pipe(take(1)).subscribe((translatedErrorMessage: string) => {
        this.messageService.add({
          severity: 'error',
          summary: this.translate.instant('errors.error'),
          detail: translatedErrorMessage,
          life: 4000
        });
      });
    });
  }

}

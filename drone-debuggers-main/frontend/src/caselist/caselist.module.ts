import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CaseTableComponent } from './case-table/case-table.component';
import {CaseListRoutingModule} from "./caselist-routing.module";
import {TableModule} from "primeng/table";
import {provideHttpClient} from "@angular/common/http";
import {TranslateModule} from "@ngx-translate/core";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ConfirmationService} from "primeng/api";
import { FlightDetailsComponent } from './case-details/components/flight-details/flight-details.component';
import { PassengerDetailsComponent } from './case-details/components/passenger-details/passenger-details.component';
import { AttachedDocumentListComponent } from './case-details/components/attached-document-list/attached-document-list.component';
import { CommentsListComponent } from './case-details/components/comments-list/comments-list.component';
import {CaseDetailsComponent} from "./case-details/components/case-details-component/case-details.component";
import { ReservationComponent } from './case-details/components/reservation/reservation.component';
import {Button} from "primeng/button";
import {FileUploadModule} from "primeng/fileupload";
import {ToastModule} from "primeng/toast";


@NgModule({
  declarations: [
    CaseTableComponent,
    FlightDetailsComponent,
    PassengerDetailsComponent,
    AttachedDocumentListComponent,
    CommentsListComponent,
    CaseDetailsComponent,
    ReservationComponent
  ],
  imports: [
    CommonModule,
    CaseListRoutingModule,
    TableModule,
    TranslateModule,
    DropdownModule,
    FormsModule,
    ConfirmDialogModule,
    Button,
    FileUploadModule,
    ToastModule

  ],
  providers: [
    ConfirmationService,
    provideHttpClient()
  ]
})
export class CaselistModule { }

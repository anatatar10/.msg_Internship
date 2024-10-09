import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';

import { CaseListAdminRoutingModule } from './case-list-admin-routing.module';
import {CaseListAdminComponent} from "./components/case-list-admin.component";
import {TranslateModule} from "@ngx-translate/core";
import {TableModule} from "primeng/table";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastModule} from "primeng/toast";
import {ConfirmPopupModule} from "primeng/confirmpopup";
import {ConfirmDialogModule} from "primeng/confirmdialog";


@NgModule({
  declarations: [CaseListAdminComponent],
  imports: [
    CommonModule,
    CaseListAdminRoutingModule,
    TranslateModule,
    TableModule,
    BrowserAnimationsModule,
    ToastModule,
    ConfirmDialogModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class CaseListAdminModule { }

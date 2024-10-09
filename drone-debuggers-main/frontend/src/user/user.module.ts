import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserRoutingModule } from './user-routing.module';
import { UserListDTOComponent } from './components/user-list-dto/user-list-dto.component';
import { TableModule } from 'primeng/table';
import { TranslateModule } from '@ngx-translate/core';
import {Button, ButtonDirective} from "primeng/button";
import { CreateColleaguePageComponent } from './components/create-colleague-page/create-colleague-page.component';
import {CaseFormPageModule} from "../case-form-page/case-form-page.module";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";

@NgModule({
  declarations: [
    UserListDTOComponent,
    CreateColleaguePageComponent
  ],
  exports: [
    UserListDTOComponent
  ],
    imports: [
        CommonModule,
        UserRoutingModule,
        TableModule,
        TranslateModule,
        ButtonDirective,
        Button,
        CaseFormPageModule,
        ConfirmDialogModule,
        ToastModule
    ]
})
export class UserModule { }

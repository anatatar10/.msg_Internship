import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './components/login.component';
import {LoginRoutingModule} from "./login-routing.module";
import {ButtonDirective} from "primeng/button";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Ripple} from "primeng/ripple";
import {SharedModule} from "../shared/shared.module";
import {TranslateModule} from "@ngx-translate/core";
import { FirstLoginComponent } from './first-login/first-login.component';



@NgModule({
  declarations: [
    LoginComponent,
    FirstLoginComponent
  ],
  imports: [
    CommonModule,
    LoginRoutingModule,
    ButtonDirective,
    FormsModule,
    ReactiveFormsModule,
    Ripple,
    SharedModule,
    TranslateModule
  ],
})
export class LoginModule { }

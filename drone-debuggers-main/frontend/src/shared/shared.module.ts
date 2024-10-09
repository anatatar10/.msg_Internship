import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ButtonComponent} from "./button/components/button.component";
import {InputComponent} from "./input/components/input.component";
import {I18nComponent} from "./i18n/components/i18n.component";
import {MessageService} from "primeng/api";

@NgModule({
  declarations: [ButtonComponent, InputComponent, I18nComponent],
    imports: [
        CommonModule,
    ],
  exports: [ButtonComponent, InputComponent, I18nComponent],
  providers: [
    MessageService
  ]
})
export class SharedModule { }

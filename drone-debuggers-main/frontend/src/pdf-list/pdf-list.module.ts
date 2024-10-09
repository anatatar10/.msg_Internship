import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PdfListRoutingModule } from './pdf-list-routing.module';
import { PdfListComponent } from './components/pdf-list.component';
import {TranslateModule} from "@ngx-translate/core";


@NgModule({
  declarations: [
    PdfListComponent
  ],
  imports: [
    CommonModule,
    PdfListRoutingModule,
    TranslateModule
  ]
})
export class PdfListModule { }

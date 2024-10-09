import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PdfDownloadRoutingModule } from './pdf-download-routing.module';
import {PdfDownloadComponent} from "./components/pdf-download.component";
import {TranslateModule} from "@ngx-translate/core";


@NgModule({
  declarations: [PdfDownloadComponent],
  imports: [
    CommonModule,
    PdfDownloadRoutingModule,
    TranslateModule,

  ]
})
export class PdfDownloadModule { }

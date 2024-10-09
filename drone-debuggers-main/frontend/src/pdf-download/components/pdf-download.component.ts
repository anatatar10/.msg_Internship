import { Component } from '@angular/core';
import { PdfDownloadService } from "../service/pdf-download.service";
import { Subscription } from "rxjs";
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-pdf-download',
  templateUrl: './pdf-download.component.html'
})
export class PdfDownloadComponent {

  caseId: undefined | number;

  constructor(private pdfDownloadService: PdfDownloadService) {}

  downloadPdf(): void {
    if(this.caseId === undefined) {
      throw new Error("Case ID is not set");
    }
    const subscription: Subscription = this.pdfDownloadService.downloadPdf(this.caseId).subscribe(
      (response) => {
        const blob = new Blob([response.body], { type: 'application/pdf' });

        // Access the Content-Disposition header
        const contentDisposition = response.headers.get('Content-Disposition');

        let fileName = `case_${this.caseId}.pdf`;

        if (contentDisposition) {
          const filenameMatch = contentDisposition.match(/filename="(.+)"/);
          if (filenameMatch && filenameMatch[1]) {
            fileName = filenameMatch[1];
          }
        }

        FileSaver.saveAs(blob, fileName);
      },
      (error) => {
        console.error('Failed to download PDF:', error);
      },
      () => {
        subscription.unsubscribe();
      }
    );
  }
}

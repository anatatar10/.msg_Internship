import {Component, OnInit} from '@angular/core';
import {GeneratedPdf} from "../model/generatedPdf";
import {PdfListService} from "../service/pdf-list.service";

@Component({
  selector: 'app-pdf-list',
  templateUrl: './pdf-list.component.html',
  styleUrl: './pdf-list.component.scss'
})
export class PdfListComponent implements OnInit{
  pdfs: GeneratedPdf[] = [];

  constructor(private pdfService: PdfListService) {}

  ngOnInit(): void {
    this.loadGeneratedPdfs();
  }

  loadGeneratedPdfs(): void {
    this.pdfService.getPdfList().subscribe({
      next: (data) => {
        this.pdfs = data;
      },
      error: (err) => {
        console.error('Error loading PDFs', err);
      }
    });
  }

}

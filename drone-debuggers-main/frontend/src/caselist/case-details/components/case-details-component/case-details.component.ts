import { Component, Input, OnInit } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import * as FileSaver from 'file-saver';
import { Reservation } from '../../models/reservation';
import { Passenger } from '../../models/passenger';
import { Comment } from '../../models/comment';
import { Flight } from "../../models/flight";
import { AttachedDocument } from "../../models/attachedDocument";
import { FlightDetailsService } from '../../services/flight-details.service';
import { PassengerDetailsService } from '../../services/passenger-details.service';
import { AttachedDocumentListService } from '../../services/attached-document-list.service';
import { CommentsListService } from '../../services/comments-list.service';
import { ReservationService } from "../../services/reservation.service";
import { CaseService } from "../../../case.service";
import { FileSelectEvent } from "primeng/fileupload";
import { Attachment } from "../../../../case-form-page/models/attachment";
import {TranslateService} from "@ngx-translate/core";



@Component({
  selector: 'app-case-details',
  templateUrl: './case-details.component.html',
  styleUrls: ['./case-details.component.scss']
})
export class CaseDetailsComponent implements OnInit {
  @Input() systemCaseId: string | null | undefined;
  flights$: Observable<Flight[]> | undefined;
  reservation$: Observable<Reservation> | undefined;
  passenger$: Observable<Passenger> | undefined;
  attachedDocuments$: Observable<AttachedDocument[]> | undefined;
  comments$: Observable<Comment[] | undefined> | undefined;
  attachedDocuments: Attachment[] = [];
  message: string = '';

  constructor(
    private route: ActivatedRoute,
    private flightService: FlightDetailsService,
    private reservationService: ReservationService,
    private passengerService: PassengerDetailsService,
    private attachedDocumentService: AttachedDocumentListService,
    private commentService: CommentsListService,
    private messageService: MessageService,
    private caseService: CaseService,
    private translate: TranslateService,
  ) {}

  ngOnInit(): void {
    this.systemCaseId = this.route.snapshot.paramMap.get('id');
    this.loadCaseDetails();
  }

  loadCaseDetails(): void {
    this.reservation$ = this.reservationService.getReservationByCaseId(this.systemCaseId);
    this.flights$ = this.flightService.getFlightsByCaseId(this.systemCaseId).pipe(tap());
    this.passenger$ = this.passengerService.getPassengerDetailsByCaseId(this.systemCaseId).pipe(tap());
    this.attachedDocuments$ = this.attachedDocumentService.getDocumentsByCaseId(this.systemCaseId).pipe(tap());
    this.comments$ = this.commentService.getCommentsByCaseId(this.systemCaseId);

  }

  loadDocuments(): void {
    this.attachedDocuments$ = this.attachedDocumentService.getDocumentsByCaseId(this.systemCaseId);
  }

  loadComments(): void {
    this.comments$ = this.commentService.getCommentsByCaseId(this.systemCaseId);
  }

  downloadDocument(document: AttachedDocument): void {
    this.attachedDocumentService.getDocumentById(document.documentId).subscribe({
      next: (response: ArrayBuffer) => {
        const blob = new Blob([response], { type: document.fileType });
        FileSaver.saveAs(blob, document.fileName);
      },
      error: (err) => {
        this.messageService.add({severity: 'error', summary: this.translate.instant('errors.error'), detail: 'Could not download the file.'});
      }
    });
  }

  onFileSelect(event: FileSelectEvent, filetype: string): void {
    // Clear previous selection
    this.attachedDocuments = [];

    for (const file of event.files) {
      const fileReader = new FileReader();

      fileReader.onload = () => {
        const uploadedDocument: Attachment = {
          documentType: filetype,
          fileName: file.name,
          fileType: file.type,
          fileData: fileReader.result!.toString().split(',')[1], // Get base64 data
          uploadDate: new Date(),
        };

        this.attachedDocuments.push(uploadedDocument);
      };

      fileReader.readAsDataURL(file);
    }
  }

  onUpload(): void {
    if (this.systemCaseId && this.attachedDocuments.length > 0) {
      this.caseService.sendCaseFiles(this.attachedDocuments, this.systemCaseId).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: this.translate.instant('user-details.success'), detail: 'All files uploaded successfully.' });
          this.attachedDocuments = []; // Clear the array after upload
          this.loadDocuments();
          this.comments$ = this.commentService.getCommentsByCaseId(this.systemCaseId);
        },
        error: (err) => {
          console.error('Upload error', err);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('errors.error'), detail: 'File upload failed.' });
        }
      });
    } else {
      this.messageService.add({ severity: 'warn', summary: this.translate.instant('errors.error'), detail: 'Please select files to upload.' });
    }
  }

  onCancelUpload(): void {
    this.attachedDocuments = [];  // Clear selected files when canceling
    this.messageService.add({ severity: 'info', summary: this.translate.instant('errors.error'), detail: 'The upload operation has been cancelled.' });
  }


  updateCharacterCount() {
  }

  sendMessage(): void {
    if (this.message.trim()) {
      let userEmail: string = sessionStorage.getItem("email")!;
      let userRole: string = sessionStorage.getItem("role")!;
      let commentSend: Comment = {
        comment: this.message,
        userEmail: userEmail,
        userRole: userRole,
        timestamp: new Date()
      };


      this.commentService.addComment(this.systemCaseId,commentSend).subscribe(
        response => {
          this.messageService.add({ severity: 'success', summary: this.translate.instant('user-details.success'), detail: 'Your message was sent successfully.' });
          this.message = ''; // Clear the textarea after sending
          this.loadComments();
        },
        error => {
          console.error('Error sending message:', error);
          this.messageService.add({ severity: 'error', summary: this.translate.instant('errors.error'), detail: 'There was an error sending your message. Please try again.' });
        }
      );
    }
  }

}

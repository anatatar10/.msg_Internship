import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import {AttachedDocument} from "../../models/attachedDocument";

@Component({
  selector: 'app-attached-document-list',
  templateUrl: './attached-document-list.component.html',
  styleUrls: ['./attached-document-list.component.scss'] // Corrected the styleUrls attribute
})
export class AttachedDocumentListComponent {
  @Input() attachedDocument: AttachedDocument | undefined;
}

import {Component, OnInit} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {FileSelectEvent} from "primeng/fileupload";
import {CaseDataService} from "../../services/case-data.service";
import {Router} from "@angular/router";
import {ViewportScroller} from "@angular/common";
import {PassengerDataService} from "../../services/passenger-data.service";
import {PassengerData} from "../../models/passenger-data";
import {take, tap} from "rxjs";
import {switchMap} from "rxjs/operators";
import {Attachment} from "../../models/attachment";
import {AttachmentSend} from "../../services/attachment-send";
import {TranslateService} from "@ngx-translate/core";
import {MessageService} from "primeng/api";
import {RolesEnum} from "../../models/enums/roles.enum";

const MinLength: number = 5;
const MaxLength: number = 100;


@Component({
  selector: 'app-case-form-passenger-details',
  templateUrl: './case-form-passenger-details.component.html',
  styleUrl: './case-form-passenger-details.component.scss'
})
export class CaseFormPassengerDetailsComponent implements OnInit {
  caseForm: FormGroup = new FormGroup({});
  allowedTypes: Set<string> = new Set(['application/pdf', 'image/jpeg', 'image/jpg']);
  maxSizeMB: number = 5;
  // Variables to store files locally
  boardingPassFile: File | null = null;
  idPassportFile: File | null = null;
  gdprFileUrl = '/assets/documents/GDPR.pdf';
  termsFileUrl = '/assets/documents/Termeni-si-conditii.pdf';
  attachments: Attachment[] = [];

  constructor(private fb: FormBuilder, private router: Router, private viewportScroller: ViewportScroller, private passengerDataService: PassengerDataService, private caseDataService: CaseDataService, private messageService: MessageService, private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.caseForm = this.fb.group({
      dateOfBirth: new FormControl('', [
        Validators.required,
        this.dateOfBirthValidator  // Custom validator to ensure the date is not in the future
      ]),
      phone: new FormControl('', [
        Validators.required,
        Validators.pattern(/^\d{10}$/) // Assuming a 10-digit phone number
      ]),
      address: new FormControl('', [
        Validators.required,
        Validators.minLength(MinLength),  // Ensure the address is at least 5 characters long
        Validators.maxLength(MaxLength)  // Limit the length of the address
      ]),
      postalCode: new FormControl('', [
        Validators.required,
        Validators.pattern(/^\d{2,}$/)
      ]),
      boardingPass: new FormControl('', [
        Validators.required,
        this.fileValidator(this.allowedTypes, this.maxSizeMB)
      ]),
      idPassport: new FormControl('', [
        Validators.required,
        this.fileValidator(this.allowedTypes, this.maxSizeMB)
      ]),
      gdprConsent: new FormControl(false, Validators.requiredTrue),
      termsAndConditions: new FormControl(false, Validators.requiredTrue)

    });
  }

  public get dateOfBirth() {
    return this.caseForm.get('dateOfBirth');
  }

  public get phone() {
    return this.caseForm.get('phone');
  }

  public get address() {
    return this.caseForm.get('address');
  }

  public get postalCode() {
    return this.caseForm.get('postalCode');
  }

  public get boardingPass() {
    return this.caseForm.get('boardingPass');
  }

  public get idPassport() {
    return this.caseForm.get('idPassport');
  }

  public get gdprConsent() {
    return this.caseForm.get('gdprConsent');
  }

  public get termsAndConditions() {
    return this.caseForm.get('termsAndConditions');
  }

// Custom validator to check file type and size
  private fileValidator(allowedTypes: Set<string>, maxSizeMB: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const file = control.value as File;

      if (!file) {
        return null; // No validation if no file is provided
      }

      const maxSizeBytes = maxSizeMB * 1024 * 1024; // Convert MB to bytes

      // Check file type
      if (!allowedTypes.has(file.type)) {
        return {invalidType: true}; // Validation error if file type is not allowed
      }

      // Check file size
      if (file.size > maxSizeBytes) {
        return {fileTooLarge: true}; // Validation error if file is too large
      }

      return null; // No validation error
    };
  }


  // Custom validator to ensure the date of birth is not in the future
  private dateOfBirthValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
      return null; // No validation if the control is empty
    }

    const dateValue = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Set time to midnight for comparison

    if (dateValue > today) {
      return {futureDate: true}; // Validation error if date is in the future
    }

    return null; // No validation error
  }

  public onSubmit(): void {
    if (this.caseForm.valid) {
      const passengerData = this.caseForm;
      const passenger: PassengerData = {
        dateOfBirth: passengerData.get('dateOfBirth')?.value,
        phoneNumber: passengerData.get('phone')?.value,
        address: passengerData.get('address')?.value,
        postalCode: passengerData.get('postalCode')?.value,
        boardingPass: passengerData.get('boardingPass')?.value,
        idPassport: passengerData.get('idPassport')?.value
      }

      this.passengerDataService.setPassengerData(passenger);
      this.caseDataService.createCompleteCase();

      this.caseDataService.sendCase().pipe(
        take(1),
        switchMap(() => {
          return this.caseDataService.sendCaseFiles(this.attachments);
        }),
        tap((systemCaseId: string) => this.caseDataService.sendPdf().subscribe())
      ).subscribe({
        next: (response) => {
          if (response) {
            this.messageService.add({
              severity: 'success',
              summary: this.translate.instant('passenger-form.success'),
              detail: this.translate.instant('passenger-form.caseCreatedSuccessfully'),
              life: 5000,
              sticky: true
            });

            setTimeout(() => {
              let route: String = '';
              if(sessionStorage.getItem("role") === RolesEnum.Nobody){
                route = '';
              }
              else if(sessionStorage.getItem("role") === RolesEnum.Passenger){
                route = '/cases';
              }
              this.router.navigate([route]).then(() => {
                this.viewportScroller.scrollToPosition([0, 0]);
              });
            }, 3000);
          }
        }
      });


    }
  }


  public onFileSelect(event: FileSelectEvent, filetype: string): void {
    const file = event.files[0]; // Assuming only one file is uploaded at a time
    let fileReader = new FileReader();
    fileReader.onload = () => {
      const uploadedDocument: Attachment = {
        documentType: filetype,
        fileName: file.name,
        fileType: file.type,
        fileData: fileReader.result!.toString().split(',')[1],
        uploadDate: new Date(),
      };

      this.attachments.push(uploadedDocument);
    }


    fileReader.readAsDataURL(file);


    if (filetype === 'Boarding Pass') {
      this.boardingPassFile = file;
      this.caseForm.get('boardingPass')?.setValue(file); // Update form control value
      this.caseForm.get('boardingPass')?.markAsTouched(); // Mark the control as touched
    } else if (filetype === 'IdPassport') {
      this.idPassportFile = file;
      this.caseForm.get('idPassport')?.setValue(file); // Update form control value
      this.caseForm.get('idPassport')?.markAsTouched(); // Mark the control as touched

    }
    this.attachments=this.attachments.filter(attachment => attachment.documentType != filetype);

  }

  public onFileRemove(filetype: string): void {
    // Reset files when removed
    if (this.caseForm.get('boardingPass')?.value && filetype === 'Boarding Pass') {
      this.boardingPassFile = null;
      this.caseForm.get('boardingPass')?.reset(); // Reset the form control
    }

    if (this.caseForm.get('idPassport')?.value && filetype === 'IdPassport') {
      this.idPassportFile = null;
      this.caseForm.get('idPassport')?.reset(); // Reset the form control
    }

    this.attachments=this.attachments.filter(attachment => attachment.documentType != filetype);


  }

  protected readonly MinLength = MinLength;
  protected readonly MaxLength = MaxLength;
}

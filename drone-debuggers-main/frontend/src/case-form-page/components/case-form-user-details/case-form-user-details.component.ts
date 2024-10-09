import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ViewportScroller } from "@angular/common";
import { Router } from "@angular/router";
import { UserDataService } from "../../services/user-data.service";
import { UserData } from "../../models/user-data";
import { CaseDataService } from "../../services/case-data.service";
import {ColleagueData} from "../../models/colleague-details";
import {ColleagueDataService} from "../../services/colleague-data.service";
import {MessageService} from "primeng/api";
import {TranslateService} from "@ngx-translate/core";
import {RolesEnum} from "../../models/enums/roles.enum";
import {Subscription} from "rxjs";
import {catchError,  switchMap} from "rxjs/operators";
import {take, tap} from "rxjs";
import { of} from "rxjs";

const MinLength: number = 2;
const MaxLength: number = 50;

@Component({
  selector: 'app-case-form-user-details',
  templateUrl: './case-form-user-details.component.html',
  styleUrls: ['./case-form-user-details.component.scss']
})
export class CaseFormUserDetailsComponent implements OnInit, OnDestroy {
  userDetailsForm: FormGroup = new FormGroup({});
  gdprFileUrl = '/assets/documents/GDPR.pdf';
  termsFileUrl = '/assets/documents/Termeni-si-conditii.pdf';
  isAdmin: boolean = false;
  isPassenger: boolean = false;
  isColleague: boolean = false;
  private subscription: Subscription = new Subscription();

  constructor(private fb: FormBuilder, private router: Router, private viewportScroller: ViewportScroller, private userDataService: UserDataService, private caseDataService: CaseDataService, private colleagueDataService: ColleagueDataService, private messageService: MessageService, private translate: TranslateService) {}

  ngOnInit(): void {
    this.isAdmin = sessionStorage.getItem('role') === RolesEnum.SysAdmin;
    this.isPassenger = sessionStorage.getItem('role') === RolesEnum.Passenger;
    this.isColleague = sessionStorage.getItem('role') === RolesEnum.Colleague;
    if(this.isPassenger){
      this.skipPage();
    }

    this.userDetailsForm = this.fb.group({
      firstName: new FormControl('', [
        Validators.required,
        Validators.minLength(MinLength), // Ensure the first name is at least 2 characters long
        Validators.maxLength(MaxLength)  // Limit the length of the first name
      ]),
      lastName: new FormControl('', [
        Validators.required,
        Validators.minLength(MinLength), // Ensure the last name is at least 2 characters long
        Validators.maxLength(MaxLength)  // Limit the length of the last name
      ]),
      email: new FormControl('', [
        Validators.required,
        Validators.pattern('^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$') // Email pattern validation
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(MinLength) // Ensure the password is at least 8 characters long
      ]),
      gdprConsent: new FormControl(false, Validators.requiredTrue),
      termsAndConditions: new FormControl(false, Validators.requiredTrue)
    });

    if(this.isAdmin || this.isColleague){
      this.userDetailsForm.get("gdprConsent")?.clearValidators();
      this.userDetailsForm.get("termsAndConditions")?.clearValidators();
    }
    if(!this.isAdmin){
      this.userDetailsForm.get("password")?.clearValidators();
    }
  }

  public get firstName() {
    return this.userDetailsForm.get('firstName');
  }

  public get lastName() {
    return this.userDetailsForm.get('lastName');
  }

  public get emailControl() {
    return this.userDetailsForm.get('email');
  }

  public get password() {
    return this.userDetailsForm.get('password');
  }

  public get gdprConsent() {
    return this.userDetailsForm.get('gdprConsent');
  }

  public get termsAndConditions() {
    return this.userDetailsForm.get('termsAndConditions');
  }

  public skipPage(){
    if(this.isPassenger){
      const user: UserData = {
        firstName: "",
        lastName: "",
        email: sessionStorage.getItem('email')?.toString() || "",
      };

      this.userDataService.setUserData(user);

      let systemCaseId = '';
      this.caseDataService.createIncompleteCase();
      this.caseDataService.sendCase().pipe(
        take(1),
        tap((systemCaseId) => {
          this.caseDataService.updateSystemCaseId(systemCaseId);
        })).subscribe()

      this.router.navigate(['/case-form/passenger-details']).then(() => {
        this.viewportScroller.scrollToPosition([0, 0]);
      });
    }
  }



  public onSubmit(): void {
    if(!this.isAdmin){
      if (this.userDetailsForm.valid) {
        const user: UserData = {
          firstName: this.firstName?.value,
          lastName: this.lastName?.value,
          email: this.emailControl?.value,
        };

        this.userDataService.setUserData(user);

        let systemCaseId = '';

        this.subscription.add(this.userDataService.createUser(user).pipe(
          take(1),
          switchMap(() => {
            this.caseDataService.createIncompleteCase();
            return this.caseDataService.sendCase();
          }),
          tap((systemCaseId) => {
            this.caseDataService.updateSystemCaseId(systemCaseId);

          }),
          catchError((error) => {
            return of(null); // Ensure the observable completes
          })
        ).subscribe({
            next: (value) => {
              if (value !== null) {
                if (this.isColleague) {
                  this.messageService.add({
                    severity: 'success',
                    summary: this.translate.instant('user-details.success'),
                    detail: this.translate.instant('user-details.userCreatedSuccessfully'),
                    life: 5000,
                    sticky: true
                  });
                  setTimeout(() => {
                    this.router.navigate(['/cases']).then(() => {
                      this.viewportScroller.scrollToPosition([0, 0]);
                    });
                  }, 3000);

                } else {
                  this.router.navigate(['/case-form/passenger-details']).then(() => {
                    this.viewportScroller.scrollToPosition([0, 0]);
                  });
                }
              }
            }
          }
        ));
        }
    }
    else{
      const colleague: ColleagueData = {
        firstName: this.firstName?.value,
        lastName: this.lastName?.value,
        email: this.emailControl?.value,
        password: this.password?.value
      };

      this.colleagueDataService.setColleagueData(colleague);

      this.subscription.add(this.colleagueDataService.createUser(colleague).subscribe({
        next: (response: string) => {
          if (response !== null) {
            this.router.navigate(["user-list"]);
            this.messageService.add({
              severity: 'success',
              detail: this.translate.instant('user-details.userCreatedSuccessfully'),
              summary: this.translate.instant('user-details.success'),
              life: 5000
            });
          }
        }
      }));

    }

  }

  protected readonly MinLength = MinLength;
  protected readonly MaxLength = MaxLength;

  ngOnDestroy(){
    this.subscription.unsubscribe();
  }
}

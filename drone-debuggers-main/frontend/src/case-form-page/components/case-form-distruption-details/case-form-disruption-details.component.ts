import {Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {DisruptionDataService} from "../../services/disruption-data.service";
import {DisruptionInfo} from "../../models/disruption-info";
import {Router} from "@angular/router";
import {ViewportScroller} from "@angular/common";
import {ReservationDataService} from "../../services/reservation-data.service";
import {CancellationType} from "../../models/enums/cancellation-type.enum";
import {Voluntary} from "../../models/enums/voluntary.enum";
import {DisruptionOption} from "../../models/enums/disruption-option.enum";
import {MentionedMotive} from "../../models/enums/mentioned-motive.enum";
import {AirlineMotive} from "../../models/enums/airline-motive.enum";


const MaxLength: number = 1024;


@Component({
  selector: 'app-case-form-distruption-details',
  templateUrl: './case-form-disruption-details.component.html',
  styleUrl: './case-form-disruption-details.component.scss'
})
export class CaseFormDisruptionDetailsComponent implements OnInit, OnDestroy {
  disruptionForm: FormGroup = new FormGroup({});
  disruptionTypes: { label: string, value: string }[] = [];
  cancellationOptions: { label: string, value: string }[] = [];
  delayOptions: { label: string, value: string }[] = [];
  deniedBoardingOptions: { label: string, value: string }[] = [];
  denialReasons: { label: string, value: string }[] = [];
  disruptionMotiveOptions: { label: string, value: string }[] = [];
  mentionedMotiveOptions: { label: string, value: string }[] = [];
  disruptionTypeSubscription: Subscription | undefined;
  possibleCompensation: number = 0;
  private translateSubscriptions: Subscription = new Subscription();

  constructor(private fb: FormBuilder, private translate: TranslateService, private disruptionDataService: DisruptionDataService, private router: Router, private viewportScroller: ViewportScroller, private reservationDataService: ReservationDataService) {
  }

  ngOnInit(): void {
    this.disruptionForm = this.fb.group({
      disruptionType: [null, Validators.required],
      cancellationInfo: this.fb.group({
        informed: ['']
      }),
      delayInfo: this.fb.group({
        delay: ['']
      }),
      deniedBoardingInfo: this.fb.group({
        voluntarily: [''],
        reason: ['']
      }),
      mentionedMotive: [''],
      motive: [''],
      additionalInfo: ['', Validators.maxLength(MaxLength)]
    }, {validators: [this.disruptionTypeValidator, this.disruptionMotiveValidator, this.eligibilityValidator]});

    let reservationFormData = this.reservationDataService.getReservationData()

    this.possibleCompensation = reservationFormData.possibleCompensation;

    this.reservationDataService.setReservationData(reservationFormData);

    this.loadTranslations();

    this.disruptionTypeSubscription = this.disruptionType?.valueChanges.subscribe(value => {
      this.resetFields();  // Reset fields whenever disruptionType changes
    });
    this.disruptionForm.updateValueAndValidity();


    this.disruptionForm.updateValueAndValidity();
    this.translateSubscriptions.add(
      this.translate.onLangChange.subscribe(() => this.loadTranslations())
    );

  }

  private loadTranslations(): void {
    // Load translations for disruption types

    const disruptionTypes$ = this.translate.instant([
      'disruption-info.disruptionTypes.cancellation',
      'disruption-info.disruptionTypes.delay',
      'disruption-info.disruptionTypes.deniedBoarding'
    ]);
    this.disruptionTypes = [
      {label: disruptionTypes$['disruption-info.disruptionTypes.cancellation'], value: CancellationType.Cancellation},
      {label: disruptionTypes$['disruption-info.disruptionTypes.delay'], value: CancellationType.Delay},
      {
        label: disruptionTypes$['disruption-info.disruptionTypes.deniedBoarding'],
        value: CancellationType.DeniedBoarding
      }
    ];

    // Load translations for other options similarly
    const cancellationOptions$ = this.translate.instant([
      'disruption-info.cancellationOptions.moreThan14Days',
      'disruption-info.cancellationOptions.lessThan14Days',
      'disruption-info.cancellationOptions.onFlightDay'
    ]);
    this.cancellationOptions = [
      {
        label: cancellationOptions$['disruption-info.cancellationOptions.moreThan14Days'],
        value: DisruptionOption.MoreThan14Days
      },
      {
        label: cancellationOptions$['disruption-info.cancellationOptions.lessThan14Days'],
        value: DisruptionOption.LessThan14Days
      },
      {
        label: cancellationOptions$['disruption-info.cancellationOptions.onFlightDay'],
        value: DisruptionOption.OnFlightDay
      }
    ];

    const delayOptions$ = this.translate.instant([
      'disruption-info.delayOptions.lessThan3Hours',
      'disruption-info.delayOptions.moreThan3Hours',
      'disruption-info.delayOptions.neverArrived'
    ])
    this.delayOptions = [
      {label: delayOptions$['disruption-info.delayOptions.lessThan3Hours'], value: DisruptionOption.LessThan3Hours},
      {label: delayOptions$['disruption-info.delayOptions.moreThan3Hours'], value: DisruptionOption.MoreThan3Hours},
      {label: delayOptions$['disruption-info.delayOptions.neverArrived'], value: DisruptionOption.NeverArrived}
    ];

    const deniedBoardingOptions$ = this.translate.instant([
      'disruption-info.responseOptions.yes',
      'disruption-info.responseOptions.no'
    ])
    this.deniedBoardingOptions = [
      {label: deniedBoardingOptions$['disruption-info.responseOptions.yes'], value: Voluntary.Yes},
      {label: deniedBoardingOptions$['disruption-info.responseOptions.no'], value: Voluntary.No}
    ];

    const motiveMentionedOptions$ = this.translate.instant([
      'disruption-info.responseOptions.yes',
      'disruption-info.responseOptions.no',
      'disruption-info.responseOptions.iDon\'tKnow'
    ])
    this.mentionedMotiveOptions = [
      {label: motiveMentionedOptions$['disruption-info.responseOptions.yes'], value: MentionedMotive.Yes},
      {label: motiveMentionedOptions$['disruption-info.responseOptions.no'], value: MentionedMotive.No},
      {label: motiveMentionedOptions$['disruption-info.responseOptions.iDon\'tKnow'], value: MentionedMotive.IDontKnow}
    ];

    const denialReasons$ = this.translate.instant([
      'disruption-info.denialReasons.flightOverbooked',
      'disruption-info.denialReasons.aggressiveBehavior',
      'disruption-info.denialReasons.intoxication',
      'disruption-info.denialReasons.unspecifiedReason'
    ])
    this.denialReasons = [
      {
        label: denialReasons$['disruption-info.denialReasons.flightOverbooked'],
        value: DisruptionOption.FlightOverbooked
      },
      {
        label: denialReasons$['disruption-info.denialReasons.aggressiveBehavior'],
        value: DisruptionOption.AggressiveBehaviour
      },
      {label: denialReasons$['disruption-info.denialReasons.intoxication'], value: DisruptionOption.Intoxication},
      {
        label: denialReasons$['disruption-info.denialReasons.unspecifiedReason'],
        value: DisruptionOption.UnspecifiedReason
      }
    ];

    const disruptionMotiveOptions$ = this.translate.instant([
      'disruption-info.disruptionMotiveOptions.technicalProblem',
      'disruption-info.disruptionMotiveOptions.meteorologicalConditions',
      'disruption-info.disruptionMotiveOptions.strike',
      'disruption-info.disruptionMotiveOptions.airportProblems',
      'disruption-info.disruptionMotiveOptions.crewProblems',
      'disruption-info.disruptionMotiveOptions.otherMotives'
    ])
    this.disruptionMotiveOptions = [
      {
        label: disruptionMotiveOptions$['disruption-info.disruptionMotiveOptions.technicalProblem'],
        value: AirlineMotive.TechnicalProblem
      },
      {
        label: disruptionMotiveOptions$['disruption-info.disruptionMotiveOptions.meteorologicalConditions'],
        value: AirlineMotive.MeteorologicalConditions
      },
      {label: disruptionMotiveOptions$['disruption-info.disruptionMotiveOptions.strike'], value: AirlineMotive.Strike},
      {
        label: disruptionMotiveOptions$['disruption-info.disruptionMotiveOptions.airportProblems'],
        value: AirlineMotive.ProblemsWithAirport
      },
      {
        label: disruptionMotiveOptions$['disruption-info.disruptionMotiveOptions.crewProblems'],
        value: AirlineMotive.CrewProblems
      },
      {
        label: disruptionMotiveOptions$['disruption-info.disruptionMotiveOptions.otherMotives'],
        value: AirlineMotive.OtherMotives
      }
    ];
  }


  public get voluntarilyValue() {
    return this.disruptionForm.get('deniedBoardingInfo')?.get('voluntarily')?.value;
  }

  public get voluntarily() {
    return this.disruptionForm.get('deniedBoardingInfo')?.get('voluntarily');
  }

  public get motive() {
    return this.disruptionForm.get('motive');
  }

  public get mentionedMotive() {
    return this.disruptionForm.get('mentionedMotive');
  }

  public get disruptionType() {
    return this.disruptionForm.get('disruptionType');
  }

  get disruptionTypeCancellationOrDelay() {
    return ['Delay', 'Cancellation'].includes(this.disruptionForm.get('disruptionType')?.value)
  }

  public get cancellationInfo() {
    return this.disruptionForm.get('cancellationInfo')?.get('informed');
  }

  public get delayInfo() {
    return this.disruptionForm.get('delayInfo')?.get('delay');
  }


  public get additionalInfo() {
    return this.disruptionForm.get('additionalInfo');
  }

  public get reason() {
    return this.disruptionForm.get('deniedBoardingInfo')?.get('reason');
  }

  public get deniedBoardingInfo() {
    return this.disruptionForm.get('deniedBoardingInfo');
  }


  private disruptionMotiveValidator(control: AbstractControl): ValidationErrors | null {
    const formGroup = control as FormGroup;
    const disruptionType = formGroup.get('disruptionType')?.value;
    const mentionedMotive = formGroup.get('mentionedMotive')?.value;
    const motive = formGroup.get('motive')?.value;

    if (!disruptionType) {
      return null; // No validation if disruptionType is not set
    }

    if (['Delay', 'Cancellation'].includes(disruptionType)) {
      if (mentionedMotive === null) {
        return {mentionedMotiveRequired: true}; // Validation error if mentionedMotive is not provided
      }
      if (mentionedMotive === MentionedMotive.Yes && !motive) {
        return {motiveRequired: true}; // Validation error if motive is required but not provided
      }
    }

    return null; // No validation error
  }

  private disruptionTypeValidator(control: AbstractControl): ValidationErrors | null {
    const formGroup = control as FormGroup;
    const disruptionType = formGroup.get('disruptionType')?.value;
    const cancellationInfo = formGroup.get('cancellationInfo')?.get('informed')?.value;
    const delayInfo = formGroup.get('delayInfo')?.get('delay')?.value;
    const deniedBoardingVoluntarily = formGroup.get('deniedBoardingInfo')?.get('voluntarily')?.value;
    const deniedBoardingReason = formGroup.get('deniedBoardingInfo')?.get('reason')?.value;

    if (!disruptionType) {
      return null; // No validation if disruptionType is not set
    }

    switch (disruptionType) {
      case CancellationType.Cancellation:
        if (!cancellationInfo) {
          return {cancellationInfoRequired: true}; // Validation error if cancellationInfo is not provided
        }
        break;

      case CancellationType.Delay:
        if (!delayInfo) {
          return {delayInfoRequired: true}; // Validation error if delayInfo is not provided
        }
        break;

      case CancellationType.DeniedBoarding:
        if (!deniedBoardingVoluntarily) {
          return {deniedBoardingVoluntarilyRequired: true}; // Validation error if voluntarily is not provided
        }
        if (deniedBoardingVoluntarily === Voluntary.No && !deniedBoardingReason) {
          return {deniedBoardingReasonRequired: true}; // Validation error if reason is required but not provided
        }
        break;
    }

    return null; // No validation error
  }

  private eligibilityValidator(control: AbstractControl): ValidationErrors | null {
    const formGroup = control as FormGroup;
    // Get values from form controls
    const disruptionType = formGroup.get('disruptionType')?.value;
    const arrivalLate = formGroup.get('delayInfo.delay')?.value;
    const cancellationNotice = formGroup.get('cancellationInfo.informed')?.value;
    const voluntarily = formGroup.get('deniedBoardingInfo.voluntarily')?.value;

    // Determine if the form is eligible based on disruption type
    let isValid = false;
    if (!disruptionType) {
      return {informationMissing: true} // Missing disruption type
    }
    if (disruptionType === CancellationType.Cancellation) {
      if (!cancellationNotice) {
        return {informationMissing: true} // Missing cancellation information
      }
      isValid = cancellationNotice === DisruptionOption.LessThan14Days || cancellationNotice === DisruptionOption.OnFlightDay;
    } else if (disruptionType === CancellationType.Delay) {
      if (!arrivalLate) {
        return {informationMissing: true} // Missing arrival
      }
      isValid = arrivalLate === DisruptionOption.MoreThan3Hours || arrivalLate === DisruptionOption.NeverArrived;
    } else if (disruptionType === CancellationType.DeniedBoarding) {
      if (!voluntarily) {
        return {informationMissing: true} // Missing voluntarily
      }
      isValid = voluntarily === Voluntary.No;
    }
    // Return validation error if not eligible
    if (isValid) {
      return null;
    }
    return {notEligible: true};
  }

  private resetFields(): void {
    this.cancellationInfo?.reset();
    this.delayInfo?.reset();
    this.deniedBoardingInfo?.reset();
    this.mentionedMotive?.reset();
    this.motive?.reset();
    this.additionalInfo?.reset();
  }

  public onSubmit(): void {
    if (this.disruptionForm.valid) {
      const disruptionType = this.disruptionType?.value;

      let disruptionOption: string = '';
      switch (disruptionType) {
        case CancellationType.Cancellation:
          disruptionOption = this.cancellationInfo?.value;
          break;

        case CancellationType.Delay:
          disruptionOption = this.delayInfo?.value;
          break;

        case CancellationType.DeniedBoarding:
          disruptionOption = this.reason?.value;
          break;
      }
      const airlineMotive = this.motive?.value;
      const incidentDescription = this.additionalInfo?.value;
      this.disruptionDataService.setDisruptionInfo({
        cancellationType: disruptionType,
        disruptionOption,
        airlineMotive,
        incidentDescription
      } as DisruptionInfo);

      this.router.navigate(['/case-form/user-details']).then(() => {
        // Scroll to top
        this.viewportScroller.scrollToPosition([0, 0]);
      });
    }
  }

  ngOnDestroy(): void {
    this.disruptionTypeSubscription?.unsubscribe();
    this.translateSubscriptions.unsubscribe();
  }

  // Used to be able to check length of form errors array
  protected readonly Object = Object;
  protected readonly CancellationType = CancellationType;
  protected readonly Voluntary = Voluntary;
}

import {Component, OnDestroy, OnInit} from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {AirportService} from '../../services/airport.service';
import {Airport} from "../../models/airport";
import {ReservationDataService} from "../../services/reservation-data.service";
import {Router} from "@angular/router";
import {ReservationData} from "../../models/reservation-data";
import {CompensationService} from "../../services/compensation.service";
import {Subscription} from "rxjs";
import {ViewportScroller} from "@angular/common";

const MaxStops: number = 3;


@Component({
  selector: 'app-case-form-reservation-details',
  templateUrl: './case-form-reservation-details.component.html',
  styleUrl: './case-form-reservation-details.component.scss'
})
export class CaseFormReservationDetailsComponent implements OnInit, OnDestroy {
  reservationForm: FormGroup = new FormGroup({});
  filteredAirports: Airport[] = []; // Changed from Observable to a simple array
  stopAirportsValue: Airport[] = [];
  private compensationSubscription: Subscription | undefined;

  constructor(private fb: FormBuilder, private airportService: AirportService, private reservationDataService: ReservationDataService, private router: Router, private compensationService: CompensationService, private viewportScroller: ViewportScroller) {
  }

  ngOnInit(): void {
    this.reservationForm = this.fb.group({
        reservationNumber: ['', [Validators.required, Validators.pattern('^[0-9A-Z]{6}$')]],
        departingAirport: ['', Validators.required],
        arrivingAirport: ['', Validators.required],
        stops: this.fb.array([])
      },
      {
        validators: this.uniqueAirportsValidator()

      });

    this.applyValidators();
    this.airportService.fetchAirports().subscribe();
  }

  private applyValidators(): void {
    const airportValidator = this.airportValidator(this.airportService);

    this.reservationForm.get('departingAirport')?.setValidators([
      Validators.required,
      airportValidator
    ]);

    this.reservationForm.get('arrivingAirport')?.setValidators([
      Validators.required,
      airportValidator
    ]);

    // Trigger validation
    this.reservationForm.get('departingAirport')?.updateValueAndValidity();
    this.reservationForm.get('arrivingAirport')?.updateValueAndValidity();
  }

  public get stops(): FormArray {
    return this.reservationForm.get('stops') as FormArray;
  }

  public get reservationNumber() {
    return this.reservationForm.get('reservationNumber');
  }

  public get departingAirport() {
    return this.reservationForm.get('departingAirport');
  }

  public get arrivingAirport() {
    return this.reservationForm.get('arrivingAirport');
  }

  createStop(): FormGroup {
    return this.fb.group({
      stop: ['', this.airportValidator(this.airportService)]
    });
  }

  private getStopValues(): Airport[] {
    let stops = this.reservationForm.value.stops;
    return (stops || [])
      .filter((stopGroup: any) => stopGroup && stopGroup.stop) // Ensure stopGroup and stop are valid
      .map((stopGroup: any) => {
        const stop = stopGroup.stop;
        return {
          airportCode: stop?.airportCode ?? '', // Default to empty string if undefined
          airportName: stop?.airportName ?? ''  // Default to empty string if undefined
        };
      });
  }


  public onSubmit(): void {
    if (this.reservationForm.valid) {

      const formValue = this.reservationForm.value;
      let stopsValues = this.getStopValues();

      this.compensationSubscription = this.compensationService.getPossibleCompensation(formValue.departingAirport.airportCode, formValue.arrivingAirport.airportCode)
        .subscribe(
          (possibleCompensation: number) => {
            const reservation: ReservationData = {
              reservationNumber: formValue.reservationNumber,
              departingAirport: formValue.departingAirport,
              arrivingAirport: formValue.arrivingAirport,
              stops: stopsValues,
              possibleCompensation: possibleCompensation
            };

            this.reservationDataService.setReservationData(reservation);
            this.router.navigate(['/case-form/flight-details']).then(() => {
              this.viewportScroller.scrollToPosition([0, 0]);
            });

          }
        );
    }
  }


  public searchAirports(query: string): void {
    if (query.length > 1) {
      this.filteredAirports = this.airportService.getFilteredAirports(query);
    }
  }


  private airportValidator(airportService: AirportService): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) {
        return null; // No validation if the control is empty
      }

      const isValid = airportService.find(control.value);
      return isValid ? null : {invalidAirport: true}; // Validation error if the airport is not in the list
    };
  }

  public addStop(): void {
    if (this.stops.length < MaxStops)
      this.stops.push(this.createStop());
  }

  public removeStop(index: number): void {
    if (this.stops.length > 0) { // Ensure at least one stop remains
      this.stops.removeAt(index);
    }
  }


  private uniqueAirportsValidator(): ValidatorFn {
    return (formGroup: AbstractControl): ValidationErrors | null => {
      const form = formGroup as FormGroup;
      const departingAirport = form.get('departingAirport')?.value;
      const arrivingAirport = form.get('arrivingAirport')?.value;

      const stopAirports = this.getStopValues();
      this.stopAirportsValue = stopAirports;

      const allAirports = [departingAirport, arrivingAirport, ...stopAirports];

      if (allAirports.filter(airport => airport !== '').length < 2) {
        return null;
      }
      const seen = new Set(allAirports.map(airport => airport.airportCode));
      if (seen.size !== allAirports.length) {
        return {nonUniqueAirports: true}; // Airports must be unique
      }

      return null;
    };
  }

  ngOnDestroy(): void {
    this.compensationSubscription?.unsubscribe();
  }
}

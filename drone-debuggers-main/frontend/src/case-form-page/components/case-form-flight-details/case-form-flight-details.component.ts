import {Component, OnInit} from '@angular/core';
import {ReservationDataService} from "../../services/reservation-data.service";
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {Airport} from "../../models/airport";
import {ReservationData} from "../../models/reservation-data";
import {FlightDataService} from "../../services/flight-data.service";
import {Router} from "@angular/router";
import {FlightInfo} from "../../models/flight-info";
import {ViewportScroller} from "@angular/common";


const MinLength: number = 2;
const MaxLength: number = 50;

@Component({
  selector: 'app-case-form-flight-details',
  templateUrl: './case-form-flight-details.component.html',
  styleUrl: './case-form-flight-details.component.scss'
})

export class CaseFormFlightDetailsComponent implements OnInit {
  flightForm: FormGroup = new FormGroup({});
  airports: Airport[] = []; // List of airport names
  reservationData: ReservationData = {} as ReservationData;
  possibleCompensation: number = 0;

  constructor(private fb: FormBuilder, private reservationDataService: ReservationDataService, private flightDataService: FlightDataService, private router: Router, private viewportScroller: ViewportScroller) {
  }

  ngOnInit(): void {
    this.reservationData = this.reservationDataService.getReservationData()

    this.airports = this.reservationData.stops;
    this.airports = this.airports.filter(airport => airport.airportCode !== this.reservationData.departingAirport.airportCode);
    this.airports = this.airports.filter(airport => airport.airportCode !== this.reservationData.arrivingAirport.airportCode);

    this.possibleCompensation = this.reservationData.possibleCompensation;
    this.airports.unshift(this.reservationData.departingAirport);
    this.airports.push(this.reservationData.arrivingAirport);
    this.initializeFlightForm();

  }

  private initializeFlightForm(): void {
    const flightArray = this.createFlightArray();
    this.flightForm = this.fb.group({
      flights: this.fb.array(flightArray, {validators: [this.flightSequenceValidator(), this.uniqueFlightNumbersValidator()]})
    });
  }


  private createFlightArray(): FormGroup[] {
    const flightGroups: FormGroup[] = [];
    for (let i = 0; i < this.airports.length - 1; i++) {
      flightGroups.push(this.createFlightGroup(this.airports[i], this.airports[i + 1]));
    }
    return flightGroups;
  }

  private createFlightGroup(from: Airport, to: Airport): FormGroup {
    return this.fb.group({
        fromAirport: [from, Validators.required],
        toAirport: [to, Validators.required],
        flightAirline: ['', [Validators.required, Validators.minLength(MinLength)]],
        flightNumber: [
          '',
          [Validators.required]
        ],
        departureTime: ['', [Validators.required, this.futureDateValidator]],
        arrivalTime: ['', [Validators.required, this.futureDateValidator]]
      },
      {validators: this.timeRangeValidator});
  }

  private uniqueFlightNumbersValidator(): ValidatorFn {
    return (formArray: AbstractControl): ValidationErrors | null => {
      const flightArray = formArray as FormArray;
      const flightNumbers = flightArray.controls.map(control => control.get('flightNumber')?.value);

      const nonEmptyFlightNumbers = flightNumbers.filter(flightNumber => flightNumber !== '');

      if (flightNumbers.some(flightNumber => flightNumber === '') && nonEmptyFlightNumbers.length < 2) {

        return null;
      }
      const uniqueFlightNumbers = new Set(flightNumbers);

      if (uniqueFlightNumbers.size !== flightNumbers.length) {
        return {duplicateFlightNumbers: true}; // Flight numbers must be unique
      }

      return null; // No validation error
    };
  }

  private timeRangeValidator(control: AbstractControl): ValidationErrors | null {
    const departureTime = control.get('departureTime')?.value;
    const arrivalTime = control.get('arrivalTime')?.value;

    if (departureTime && arrivalTime) {
      const departureDate = new Date(departureTime);
      const arrivalDate = new Date(arrivalTime);

      if (departureDate > arrivalDate) {
        return {invalidTimeRange: true}; // Departure must be before arrival
      }
    }

    return null;
  }

// Validator to ensure that the date is not in the future
  private futureDateValidator(control: AbstractControl): ValidationErrors | null {
    const dateValue = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Set time to midnight for comparison

    if (dateValue > today) {
      return {futureDate: true}; // Date cannot be in the future
    }

    return null; // No validation error
  }

  public get flights(): FormArray {
    return this.flightForm.get('flights') as FormArray;
  }

  public getFlightFormGroup(index: number): FormGroup {
    return this.flights.at(index) as FormGroup;
  }


  public onSubmit(): void {
    if (this.flightForm.valid) {
      // Convert form data to FlightInfo

      const flights: FlightInfo[] = this.flights.value.map((flight: any) => {
        // Create FlightInfo instance
        return new FlightInfo(
          flight.flightNumber,
          flight.fromAirport,
          flight.toAirport,
          new Date(flight.departureTime),
          new Date(flight.arrivalTime),
          flight.flightAirline,
          false
        );
      });

      this.reservationDataService.setReservationData(this.reservationData);

      if (flights.length == 1) {
        flights[0].isProblematic = true;
      }

      this.flightDataService.setFlights(flights);

      if (flights.length > 1) {
        this.router.navigate(['/case-form/problem-flight-details']).then(() => {
          // Scroll to top
          this.viewportScroller.scrollToPosition([0, 0]);
        });
      } else {
        this.router.navigate(['/case-form/disruption-details']).then(() => {
          // Scroll to top
          this.viewportScroller.scrollToPosition([0, 0]);
        });
      }
    }
  }

  private flightSequenceValidator(): ValidatorFn {
    return (formArray: AbstractControl): ValidationErrors | null => {
      const flights = (formArray as FormArray).controls as FormGroup[];
      if (flights.length === 1) {
        return null;
      }
      for (let i = 0; i < flights.length - 1; i++) {
        const currentFlight = flights[i];
        const nextFlight = flights[i + 1];

        const currentArrivalTime = new Date(currentFlight.get('arrivalTime')?.value);
        const nextDepartureTime = new Date(nextFlight.get('departureTime')?.value);

        if (currentArrivalTime > nextDepartureTime) {
          return {invalidFlightSequence: true}; // Arrival time of a flight must be before the departure time of the next flight
        }
      }

      return null; // No validation error
    };
  }

}

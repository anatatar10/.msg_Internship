import {Component, OnDestroy, OnInit} from '@angular/core';
import {FlightDataService} from "../../services/flight-data.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ViewportScroller} from "@angular/common";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-case-form-problem-flight-details',
  templateUrl: './case-form-problem-flight-details.component.html',
  styleUrl: './case-form-problem-flight-details.component.scss'
})
export class CaseFormProblemFlightDetailsComponent implements OnInit, OnDestroy {
  flights: any[] = [];
  flightForm: FormGroup = new FormGroup({});
  selectedFlightId: number | null = null;
  subscriptions: Subscription = new Subscription();

  constructor(private flightDataService: FlightDataService, private fb: FormBuilder, private router: Router, private viewPortScroller: ViewportScroller) {
  }

  ngOnInit() {
    this.flightForm = this.fb.group({
      selectedFlight: [null, Validators.required]
    });
    this.flights = this.flightDataService.getFlights();
  }


  public get selectedFlightIndexControl() {
    return this.flightForm.get('selectedFlight');
  }

  public onSelectFlight(flightId: number) {
    this.selectedFlightId = flightId;
    this.selectedFlightIndexControl?.setValue(flightId);
  }

  public onSubmit() {
    if (this.flightForm.valid) {
      const selectedFlightId = this.flightForm.value.selectedFlight;

      this.flights.forEach((flight, index) => {
        flight.isProblematic = (index === selectedFlightId);
      });

      this.flightDataService.setFlights(this.flights);
      this.router.navigate(['/case-form/disruption-details']).then(r => {
        this.viewPortScroller.scrollToPosition([0, 0]);
      });
    }
  }

  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }
}

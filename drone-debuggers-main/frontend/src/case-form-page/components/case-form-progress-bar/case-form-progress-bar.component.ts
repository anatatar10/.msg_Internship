import {Component, OnDestroy, OnInit} from '@angular/core';
import {Route, Router} from "@angular/router";
import {Subscription} from "rxjs";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-case-form-progress-bar',
  templateUrl: './case-form-progress-bar.component.html',
  styleUrl: './case-form-progress-bar.component.scss'
})
export class CaseFormProgressBarComponent implements OnInit, OnDestroy {
  checkpoints = [
    {
      number: 1,
      title: "Reservation Information",
      link: '/case-form/reservation-details',
      completed: false,
      isActive: false
    },
    {number: 2, title: "Flight Information", link: '/case-form/flight-details', completed: false, isActive: false},
    {
      number: 3,
      title: "Disruption Information",
      link: '/case-form/disruption-details',
      completed: false,
      isActive: false
    },
    {number: 4, title: "User Information", link: '/case-form/user-details', completed: false, isActive: false},
    {number: 5, title: "Passenger Information", link: '/case-form/passenger-details', completed: false, isActive: false}
  ];
  subscriptions = new Subscription();

  currentLocation: string = '';

  constructor(private router: Router, private translateService: TranslateService) {

  }

  ngOnInit(): void {
    // Subscribe to router events to update the steps
    this.subscriptions.add(this.router.events.subscribe(() => {
      this.updateLocation();
    }));

    // Initial update
    this.updateLocation();


    this.translateCheckpoints();
    this.subscriptions.add(
      this.translateService.onLangChange.subscribe(() => this.translateCheckpoints())
    );

  }

  private translateCheckpoints(): void {
    this.checkpoints[0].title = this.translateService.instant('progress-bar.reservationInformation');
    this.checkpoints[1].title = this.translateService.instant('progress-bar.flightInformation');
    this.checkpoints[2].title = this.translateService.instant('progress-bar.disruptionInformation');
    this.checkpoints[3].title = this.translateService.instant('progress-bar.userInformation');
    this.checkpoints[4].title = this.translateService.instant('progress-bar.passengerInformation');
  }

  private updateLocation(): void {
    this.currentLocation = this.router.url;

    if (this.currentLocation === '/case-form/problem-flight-details') {
      this.currentLocation = '/case-form/flight-details';
    }

    let completed = false;
    // Find the index of the current checkpoint
    const currentIndex = this.checkpoints.findIndex(checkpoint => checkpoint.link === this.currentLocation);

    // Update checkpoints based on their position relative to the current location
    this.checkpoints = this.checkpoints.map((checkpoint, index) => {
      if (index === currentIndex) {
        // The current location should be marked as active
        checkpoint.isActive = true;
        checkpoint.completed = false; // Current location is active, not completed
      } else if (index < currentIndex) {
        // Checkpoints before the current location are completed
        checkpoint.completed = true;
        checkpoint.isActive = false; // Not active
      } else {
        // Checkpoints after the current location are not completed
        checkpoint.completed = false;
        checkpoint.isActive = false; // Not active
      }
      return checkpoint;
    });
  }


  ngOnDestroy() {
    this.subscriptions.unsubscribe();
  }

}

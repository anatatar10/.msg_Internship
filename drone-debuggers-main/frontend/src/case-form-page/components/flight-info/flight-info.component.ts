import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {Airport} from "../../models/airport";

const MinLength: number = 2;
const MaxLength: number = 50;

@Component({
  selector: 'app-flight-info',
  templateUrl: './flight-info.component.html',
  styleUrl: './flight-info.component.scss'
})
export class FlightInfoComponent {
  @Input() flightFormGroup!: FormGroup;
  @Input() selected: boolean = false;

  public getAirportDisplay(airport: Airport | null): string {
    if (airport) {
      return `${airport.airportName}, ${airport.airportCode}`;
    }
    return '';
  }

  public get departureAirport() {
    return this.flightFormGroup.get('fromAirport');
  }

  public get arrivalAirport() {
    return this.flightFormGroup.get('toAirport');
  }


  public get airline() {
    return this.flightFormGroup.get('flightAirline');
  }

  public get flightNumber() {
    return this.flightFormGroup.get('flightNumber');
  }

  public get departureTime() {
    return this.flightFormGroup.get('departureTime');
  }

  public get arrivalTime() {
    return this.flightFormGroup.get('arrivalTime');
  }

  protected readonly MinLength = MinLength;
}

import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Airport} from "../../models/airport";
import {FlightInfo} from "../../models/flight-info";

@Component({
  selector: 'app-problem-flight-info',
  templateUrl: './problem-flight-info.component.html',
  styleUrl: './problem-flight-info.component.scss'
})
export class ProblemFlightInfoComponent {
  @Input() flight!: FlightInfo; // Assume Flight is a model with appropriate properties
  @Input() selected: boolean = false;
  @Output() selectFlight = new EventEmitter<void>();

  public onSelect() {
    this.selectFlight.emit();
  }

  public getAirportDisplay(airport: Airport | null): string {
    if (airport) {
      return `${airport.airportName}, ${airport.airportCode}`;
    }
    return '';
  }


  public getAirportCode(airport: Airport): string {
    return airport.airportCode;
  }

}

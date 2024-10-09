import {Component, Input} from '@angular/core';
import { Reservation } from '../../models/reservation';
import {Flight} from "../../models/flight";

@Component({
  selector: 'app-flight-details',
  templateUrl: './flight-details.component.html',
  styleUrls: ['./flight-details.component.scss']
})
export class FlightDetailsComponent{
  @Input() flightReservation: Flight | undefined;
}

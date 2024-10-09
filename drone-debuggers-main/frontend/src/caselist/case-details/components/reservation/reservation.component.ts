import {Component, Input} from '@angular/core';
import {Reservation} from "../../models/reservation";

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.scss'
})
export class ReservationComponent {
  @Input() reservation: Reservation | undefined;
}

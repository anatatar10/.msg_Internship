import {Component, Input} from '@angular/core';

import { Passenger } from '../../models/passenger';

@Component({
  selector: 'app-passenger-details',
  templateUrl: './passenger-details.component.html',
  styleUrls: ['./passenger-details.component.scss']
})
export class PassengerDetailsComponent {
  @Input() passenger: Passenger | undefined;
}

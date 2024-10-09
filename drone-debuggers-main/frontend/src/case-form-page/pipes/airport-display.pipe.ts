import { Pipe, PipeTransform } from '@angular/core';
import {Airport} from "../models/airport";

@Pipe({
  name: 'airportDisplay'
})
export class AirportDisplayPipe implements PipeTransform {

  transform(airport: Airport | null): string {
    if (airport) {
      return `${airport.airportName}, ${airport.airportCode}`;
    }
    return '';
  }

}

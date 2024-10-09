import {Injectable, OnDestroy} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of, throwError, timer, from, zip, concat, Subscription} from 'rxjs';
import {catchError, concatMap, map, reduce, switchMap, tap} from 'rxjs/operators';
import {Airport} from '../models/airport'; // Adjust the path to your Airport interface

@Injectable({
  providedIn: 'root'
})
export class AirportService{
  private airports: Airport[] = [];
  sampleAirports: Airport[] = [
    {
      airportCode: 'GKA',
      airportName: 'Goroka Airport'
    },
    {
      airportCode: 'MAG',
      airportName: 'Madang Airport'
    },
    {
      airportCode: 'YBR',
      airportName: 'Brandon Municipal Airport'
    },
    {
      airportCode: 'YCB',
      airportName: 'Cambridge Bay Airport'
    },
    {
      airportCode: 'LAX',
      airportName: 'Los Angeles International Airport'
    },
    {
      airportCode: 'JFK',
      airportName: 'John F. Kennedy International Airport'
    },
    {
      airportCode: 'ORD',
      airportName: 'O\'Hare International Airport'
    },
    {
      airportCode: 'ATL',
      airportName: 'Hartsfield-Jackson Atlanta International Airport'
    },
    {
      airportCode: 'DFW',
      airportName: 'Dallas/Fort Worth International Airport'
    },
    {
      airportCode: 'SFO',
      airportName: 'San Francisco International Airport'
    }
  ];

  constructor(private http: HttpClient) {
  }

  /**
   * Fetches a list of airports from the API.
   * @returns An Observable of Airport objects.
   */
  public fetchAirports(): Observable<Airport[]> {
    return this.http.get<Airport[]>('/api/airport/all').pipe(
      tap(airports => this.airports = airports),
      catchError(error => {
        console.error('Error fetching airports', error);
        return of([]);
      })
    );
  }

  public getSampleAirports(): Airport[] {
    return this.sampleAirports;
  }


  public getFilteredAirports(searchTerm: string): Airport[] {
    if (searchTerm.length < 2) {
      return this.airports;
    }
    // Normalize the search term to lower case
    const normalizedTerm = searchTerm.toLowerCase();
    // Filter the airports based on the normalized search term
    return this.airports.filter(airport =>
      airport.airportName.toLowerCase().includes(normalizedTerm) ||
      airport.airportCode.toLowerCase().includes(normalizedTerm)
    );
  }


  public find(value: Airport) {
    return this.airports.find(airport => airport.airportCode === value.airportCode || airport.airportName === value.airportName);

  }
}

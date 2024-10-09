import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CaseFormReservationDetailsComponent} from './case-form-reservation-details.component';

describe('CaseFormReservationDetailsComponent', () => {
  let component: CaseFormReservationDetailsComponent;
  let fixture: ComponentFixture<CaseFormReservationDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CaseFormReservationDetailsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CaseFormReservationDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

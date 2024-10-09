import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CaseFormFlightDetailsComponent} from './case-form-flight-details.component';

describe('CaseFormFlightDetailsComponent', () => {
  let component: CaseFormFlightDetailsComponent;
  let fixture: ComponentFixture<CaseFormFlightDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CaseFormFlightDetailsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CaseFormFlightDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

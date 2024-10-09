import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CaseFormProblemFlightDetailsComponent} from './case-form-problem-flight-details.component';

describe('CaseFormProblemFlightDetailsComponent', () => {
  let component: CaseFormProblemFlightDetailsComponent;
  let fixture: ComponentFixture<CaseFormProblemFlightDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CaseFormProblemFlightDetailsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CaseFormProblemFlightDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

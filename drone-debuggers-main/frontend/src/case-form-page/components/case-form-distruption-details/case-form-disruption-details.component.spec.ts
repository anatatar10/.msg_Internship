import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CaseFormDisruptionDetailsComponent} from './case-form-disruption-details.component';

describe('CaseFormDistruptionDetailsComponent', () => {
  let component: CaseFormDisruptionDetailsComponent;
  let fixture: ComponentFixture<CaseFormDisruptionDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CaseFormDisruptionDetailsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CaseFormDisruptionDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

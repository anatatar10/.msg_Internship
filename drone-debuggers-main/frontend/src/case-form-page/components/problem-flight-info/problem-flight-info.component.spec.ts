import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ProblemFlightInfoComponent} from './problem-flight-info.component';

describe('ProblemFlightInfoComponent', () => {
  let component: ProblemFlightInfoComponent;
  let fixture: ComponentFixture<ProblemFlightInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProblemFlightInfoComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ProblemFlightInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

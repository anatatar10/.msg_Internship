import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CaseFormProgressBarComponent} from './case-form-progress-bar.component';

describe('CaseFormProgressBarComponent', () => {
  let component: CaseFormProgressBarComponent;
  let fixture: ComponentFixture<CaseFormProgressBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CaseFormProgressBarComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CaseFormProgressBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

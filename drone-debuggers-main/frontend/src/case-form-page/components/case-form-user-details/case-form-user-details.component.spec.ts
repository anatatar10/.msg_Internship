import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CaseFormUserDetailsComponent} from './case-form-user-details.component';

describe('CaseFormUserDetailsComponent', () => {
  let component: CaseFormUserDetailsComponent;
  let fixture: ComponentFixture<CaseFormUserDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CaseFormUserDetailsComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CaseFormUserDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

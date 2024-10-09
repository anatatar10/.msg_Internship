import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CaseFormWrapperComponent} from './case-form-wrapper.component';

describe('CaseFormWrapperComponent', () => {
  let component: CaseFormWrapperComponent;
  let fixture: ComponentFixture<CaseFormWrapperComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CaseFormWrapperComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CaseFormWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

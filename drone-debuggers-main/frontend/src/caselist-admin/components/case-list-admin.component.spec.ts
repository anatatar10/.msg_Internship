import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseListAdminComponent } from './case-list-admin.component';

describe('CaseListAdminComponent', () => {
  let component: CaseListAdminComponent;
  let fixture: ComponentFixture<CaseListAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CaseListAdminComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CaseListAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

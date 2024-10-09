import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttachedDocumentListComponent } from './attached-document-list.component';

describe('AttachedDocumentListComponent', () => {
  let component: AttachedDocumentListComponent;
  let fixture: ComponentFixture<AttachedDocumentListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AttachedDocumentListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AttachedDocumentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

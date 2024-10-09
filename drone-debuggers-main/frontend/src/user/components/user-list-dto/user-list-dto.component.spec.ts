import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserListDTOComponent } from './user-list-dto.component';

describe('UserListDTOComponent', () => {
  let component: UserListDTOComponent;
  let fixture: ComponentFixture<UserListDTOComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserListDTOComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserListDTOComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

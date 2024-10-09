import { TestBed } from '@angular/core/testing';

import { CaselistAdminService } from './caselist-admin.service';

describe('CaselistAdminService', () => {
  let service: CaselistAdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CaselistAdminService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

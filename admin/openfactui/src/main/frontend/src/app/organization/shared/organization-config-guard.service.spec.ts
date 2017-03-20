/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { OrganizationConfigGuardService } from './organization-config-guard.service';

describe('OrganizationConfigGuardService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrganizationConfigGuardService]
    });
  });

  it('should ...', inject([OrganizationConfigGuardService], (service: OrganizationConfigGuardService) => {
    expect(service).toBeTruthy();
  }));
});

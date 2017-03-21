/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CurrentOrganizationService } from './current-organization.service';

describe('CurrentOrganizationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CurrentOrganizationService]
    });
  });

  it('should ...', inject([CurrentOrganizationService], (service: CurrentOrganizationService) => {
    expect(service).toBeTruthy();
  }));
});

/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { OrganizationSunatService } from './organization-sunat.service';

describe('OrganizationSunatService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrganizationSunatService]
    });
  });

  it('should ...', inject([OrganizationSunatService], (service: OrganizationSunatService) => {
    expect(service).toBeTruthy();
  }));
});

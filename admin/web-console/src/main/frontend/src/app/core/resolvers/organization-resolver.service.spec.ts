/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { OrganizationResolverService } from './organization-resolver.service';

describe('OrganizationResolverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrganizationResolverService]
    });
  });

  it('should ...', inject([OrganizationResolverService], (service: OrganizationResolverService) => {
    expect(service).toBeTruthy();
  }));
});

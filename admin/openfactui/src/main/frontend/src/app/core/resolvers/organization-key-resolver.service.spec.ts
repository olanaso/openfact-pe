/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { OrganizationKeyResolverService } from './organization-key-resolver.service';

describe('OrganizationKeyResolverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrganizationKeyResolverService]
    });
  });

  it('should ...', inject([OrganizationKeyResolverService], (service: OrganizationKeyResolverService) => {
    expect(service).toBeTruthy();
  }));
});

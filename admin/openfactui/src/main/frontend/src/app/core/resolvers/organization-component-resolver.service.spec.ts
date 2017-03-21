/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { OrganizationComponentResolverService } from './organization-component-resolver.service';

describe('OrganizationComponentResolverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OrganizationComponentResolverService]
    });
  });

  it('should ...', inject([OrganizationComponentResolverService], (service: OrganizationComponentResolverService) => {
    expect(service).toBeTruthy();
  }));
});

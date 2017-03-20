/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ServerInfoResolverService } from './server-info-resolver.service';

describe('ServerInfoResolverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ServerInfoResolverService]
    });
  });

  it('should ...', inject([ServerInfoResolverService], (service: ServerInfoResolverService) => {
    expect(service).toBeTruthy();
  }));
});

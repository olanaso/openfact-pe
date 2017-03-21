/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { EventsConfigResolverService } from './events-config-resolver.service';

describe('EventsConfigResolverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EventsConfigResolverService]
    });
  });

  it('should ...', inject([EventsConfigResolverService], (service: EventsConfigResolverService) => {
    expect(service).toBeTruthy();
  }));
});

/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { RetentionService } from './retention.service';

describe('RetentionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RetentionService]
    });
  });

  it('should ...', inject([RetentionService], (service: RetentionService) => {
    expect(service).toBeTruthy();
  }));
});

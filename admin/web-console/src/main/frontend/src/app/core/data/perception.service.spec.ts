/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { PerceptionService } from './perception.service';

describe('PerceptionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PerceptionService]
    });
  });

  it('should ...', inject([PerceptionService], (service: PerceptionService) => {
    expect(service).toBeTruthy();
  }));
});

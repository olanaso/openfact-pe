/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { RestangularService } from './restangular.service';

describe('RestangularService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RestangularService]
    });
  });

  it('should ...', inject([RestangularService], (service: RestangularService) => {
    expect(service).toBeTruthy();
  }));
});

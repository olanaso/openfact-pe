/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { DocumentResolverService } from './document-resolver.service';

describe('DocumentResolverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DocumentResolverService]
    });
  });

  it('should ...', inject([DocumentResolverService], (service: DocumentResolverService) => {
    expect(service).toBeTruthy();
  }));
});

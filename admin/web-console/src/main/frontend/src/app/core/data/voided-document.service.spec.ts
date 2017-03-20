/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { VoidedDocumentService } from './voided-document.service';

describe('VoidedDocumentService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [VoidedDocumentService]
    });
  });

  it('should ...', inject([VoidedDocumentService], (service: VoidedDocumentService) => {
    expect(service).toBeTruthy();
  }));
});

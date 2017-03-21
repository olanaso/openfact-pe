/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CreditNoteService } from './credit-note.service';

describe('CreditNoteService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CreditNoteService]
    });
  });

  it('should ...', inject([CreditNoteService], (service: CreditNoteService) => {
    expect(service).toBeTruthy();
  }));
});

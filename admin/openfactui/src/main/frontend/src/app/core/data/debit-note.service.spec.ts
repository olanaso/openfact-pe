/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { DebitNoteService } from './debit-note.service';

describe('DebitNoteService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DebitNoteService]
    });
  });

  it('should ...', inject([DebitNoteService], (service: DebitNoteService) => {
    expect(service).toBeTruthy();
  }));
});

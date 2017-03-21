import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { AlertService } from './../../../core/alert/alert.service';
import { Model } from '../../../core/model/model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

@Component({
  selector: 'of-button-delete',
  templateUrl: './button-delete.component.html',
  styleUrls: []
})
export class ButtonDeleteComponent implements OnInit {

  // Object to be deleted
  @Input()
  model: Model;

  // Resource Kind to delete (e.g., 'Pod' or 'ReplicationController').
  @Input()
  kind: string;

  // Optional display name for kind.
  @Input()
  typeDisplayName: string;

  // Optional display name of the resource to delete.
  @Input()
  displayName: string;

  // Name of the resource to delete.
  @Input()
  resourceName: string;

  // Set to true to disable the delete button.
  @Input()
  disableDelete = false;

  // Force the user to enter the name before we'll delete the resource (e.g. for projects).
  @Input()
  typeNameToConfirm = false;

  // Optional link label. Defaults to 'Delete'.
  @Input()
  label: string;

  // Only show a delete icon with no text.
  @Input()
  buttonOnly: boolean;

  // Stay on the current page without redirecting to the resource list.
  @Input()
  stayOnCurrentPage = true;

  // Optional callback when the delete succeeds
  @Output()
  success: EventEmitter<boolean> = new EventEmitter<boolean>();

  // Optional redirect URL when the delete succeeds
  @Input()
  redirectUrl: string;

  confirmName = '';

  constructor(
    private router: Router,
    private modalService: NgbModal,
    private alertService: AlertService
  ) { }

  ngOnInit() {
  }

  isOrganization(): boolean {
    if (this.kind === 'Organization') {
      return true;
    }
    return false;
  }

  openDeleteModal(confirmDeleteContent: any): void {
    if (this.disableDelete) {
      return;
    }

    // opening the modal with settings scope as parent
    this.modalService.open(confirmDeleteContent).result.then(
      (result) => {
        this.model.restangular.delete().subscribe(
          (data) => {
            this.alertService.pop('success', 'Success', 'Success! The organization has been deleted.');

            // callback
            this.success.emit(true);

            // navigate
            if (!this.stayOnCurrentPage) {
              this.router.navigate([this.redirectUrl]);
            }
          }
        );
      },
      (reason) => { }
    );
  }

}

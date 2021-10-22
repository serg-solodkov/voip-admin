import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {IVendor} from "../../../vendor/vendor.model";


@Component({
  templateUrl: './vendor-change-dialog.component.html',
})
export class VendorChangeDialogComponent {
  oldValue?: IVendor;
  newValue?: IVendor;

  constructor(public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.close(this.oldValue);
  }

  confirmVendorChange(): void {
    this.activeModal.close(this.newValue);
  }
}

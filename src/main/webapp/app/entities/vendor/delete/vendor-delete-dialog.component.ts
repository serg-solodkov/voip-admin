import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVendor } from '../vendor.model';
import { VendorService } from '../service/vendor.service';

@Component({
  templateUrl: './vendor-delete-dialog.component.html',
})
export class VendorDeleteDialogComponent {
  vendor?: IVendor;

  constructor(protected vendorService: VendorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vendorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

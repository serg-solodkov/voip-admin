import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOtherDeviceType } from '../other-device-type.model';
import { OtherDeviceTypeService } from '../service/other-device-type.service';

@Component({
  templateUrl: './other-device-type-delete-dialog.component.html',
})
export class OtherDeviceTypeDeleteDialogComponent {
  otherDeviceType?: IOtherDeviceType;

  constructor(protected otherDeviceTypeService: OtherDeviceTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.otherDeviceTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

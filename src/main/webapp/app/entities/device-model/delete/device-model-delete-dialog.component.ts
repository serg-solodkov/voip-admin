import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeviceModel } from '../device-model.model';
import { DeviceModelService } from '../service/device-model.service';

@Component({
  templateUrl: './device-model-delete-dialog.component.html',
})
export class DeviceModelDeleteDialogComponent {
  deviceModel?: IDeviceModel;

  constructor(protected deviceModelService: DeviceModelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deviceModelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

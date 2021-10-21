import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {IDeviceModel} from "../../../device-model/device-model.model";


@Component({
  templateUrl: './device-model-change-dialog.component.html',
})
export class DeviceModelChangeDialogComponent {
  oldValue?: IDeviceModel;
  newValue?: IDeviceModel;

  constructor(public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.close(this.oldValue);
  }

  confirmModelChange(): void {
    this.activeModal.close(this.newValue);
  }
}

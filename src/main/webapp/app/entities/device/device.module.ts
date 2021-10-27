import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeviceComponent } from './list/device.component';
import { DeviceDetailComponent } from './detail/device-detail.component';
import { DeviceUpdateComponent } from './update/device-update.component';
import { DeviceDeleteDialogComponent } from './delete/device-delete-dialog.component';
import { DeviceRoutingModule } from './route/device-routing.module';
import { DeviceModelChangeDialogComponent } from "./update/device-model-change-dialog/device-model-change-dialog.component";
import { NgSelectModule } from "@ng-select/ng-select";

@NgModule({
  imports: [
    SharedModule,
    DeviceRoutingModule,
    NgSelectModule
  ],
  declarations: [
    DeviceComponent,
    DeviceDetailComponent,
    DeviceUpdateComponent,
    DeviceDeleteDialogComponent,
    DeviceModelChangeDialogComponent
  ],
  entryComponents: [DeviceDeleteDialogComponent],
})
export class DeviceModule {}

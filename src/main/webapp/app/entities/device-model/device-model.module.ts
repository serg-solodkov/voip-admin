import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeviceModelComponent } from './list/device-model.component';
import { DeviceModelDetailComponent } from './detail/device-model-detail.component';
import { DeviceModelUpdateComponent } from './update/device-model-update.component';
import { DeviceModelDeleteDialogComponent } from './delete/device-model-delete-dialog.component';
import { DeviceModelRoutingModule } from './route/device-model-routing.module';
import { NgSelectModule } from "@ng-select/ng-select";
import { VendorChangeDialogComponent } from "./update/vendor-change-dialog/vendor-change-dialog.component";

@NgModule({
  imports: [
    SharedModule,
    DeviceModelRoutingModule,
    NgSelectModule
  ],
  declarations: [
    DeviceModelComponent,
    DeviceModelDetailComponent,
    DeviceModelUpdateComponent,
    DeviceModelDeleteDialogComponent,
    VendorChangeDialogComponent
  ],
  entryComponents: [DeviceModelDeleteDialogComponent],
})
export class DeviceModelModule {}

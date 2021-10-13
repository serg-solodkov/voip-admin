import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeviceModelComponent } from './list/device-model.component';
import { DeviceModelDetailComponent } from './detail/device-model-detail.component';
import { DeviceModelUpdateComponent } from './update/device-model-update.component';
import { DeviceModelDeleteDialogComponent } from './delete/device-model-delete-dialog.component';
import { DeviceModelRoutingModule } from './route/device-model-routing.module';

@NgModule({
  imports: [SharedModule, DeviceModelRoutingModule],
  declarations: [DeviceModelComponent, DeviceModelDetailComponent, DeviceModelUpdateComponent, DeviceModelDeleteDialogComponent],
  entryComponents: [DeviceModelDeleteDialogComponent],
})
export class DeviceModelModule {}

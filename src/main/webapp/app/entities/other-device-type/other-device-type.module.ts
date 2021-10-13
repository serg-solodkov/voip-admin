import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OtherDeviceTypeComponent } from './list/other-device-type.component';
import { OtherDeviceTypeDetailComponent } from './detail/other-device-type-detail.component';
import { OtherDeviceTypeUpdateComponent } from './update/other-device-type-update.component';
import { OtherDeviceTypeDeleteDialogComponent } from './delete/other-device-type-delete-dialog.component';
import { OtherDeviceTypeRoutingModule } from './route/other-device-type-routing.module';

@NgModule({
  imports: [SharedModule, OtherDeviceTypeRoutingModule],
  declarations: [
    OtherDeviceTypeComponent,
    OtherDeviceTypeDetailComponent,
    OtherDeviceTypeUpdateComponent,
    OtherDeviceTypeDeleteDialogComponent,
  ],
  entryComponents: [OtherDeviceTypeDeleteDialogComponent],
})
export class OtherDeviceTypeModule {}

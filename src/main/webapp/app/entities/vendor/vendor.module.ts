import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VendorComponent } from './list/vendor.component';
import { VendorDetailComponent } from './detail/vendor-detail.component';
import { VendorUpdateComponent } from './update/vendor-update.component';
import { VendorDeleteDialogComponent } from './delete/vendor-delete-dialog.component';
import { VendorRoutingModule } from './route/vendor-routing.module';

@NgModule({
  imports: [SharedModule, VendorRoutingModule],
  declarations: [VendorComponent, VendorDetailComponent, VendorUpdateComponent, VendorDeleteDialogComponent],
  entryComponents: [VendorDeleteDialogComponent],
})
export class VendorModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OptionValueComponent } from './list/option-value.component';
import { OptionValueDetailComponent } from './detail/option-value-detail.component';
import { OptionValueUpdateComponent } from './update/option-value-update.component';
import { OptionValueDeleteDialogComponent } from './delete/option-value-delete-dialog.component';
import { OptionValueRoutingModule } from './route/option-value-routing.module';

@NgModule({
  imports: [SharedModule, OptionValueRoutingModule],
  declarations: [OptionValueComponent, OptionValueDetailComponent, OptionValueUpdateComponent, OptionValueDeleteDialogComponent],
  entryComponents: [OptionValueDeleteDialogComponent],
})
export class OptionValueModule {}

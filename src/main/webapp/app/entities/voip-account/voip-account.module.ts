import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VoipAccountComponent } from './list/voip-account.component';
import { VoipAccountDetailComponent } from './detail/voip-account-detail.component';
import { VoipAccountUpdateComponent } from './update/voip-account-update.component';
import { VoipAccountDeleteDialogComponent } from './delete/voip-account-delete-dialog.component';
import { VoipAccountRoutingModule } from './route/voip-account-routing.module';

@NgModule({
  imports: [SharedModule, VoipAccountRoutingModule],
  declarations: [VoipAccountComponent, VoipAccountDetailComponent, VoipAccountUpdateComponent, VoipAccountDeleteDialogComponent],
  entryComponents: [VoipAccountDeleteDialogComponent],
})
export class VoipAccountModule {}

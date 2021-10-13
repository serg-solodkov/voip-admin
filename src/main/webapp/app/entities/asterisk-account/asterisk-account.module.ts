import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AsteriskAccountComponent } from './list/asterisk-account.component';
import { AsteriskAccountDetailComponent } from './detail/asterisk-account-detail.component';
import { AsteriskAccountUpdateComponent } from './update/asterisk-account-update.component';
import { AsteriskAccountDeleteDialogComponent } from './delete/asterisk-account-delete-dialog.component';
import { AsteriskAccountRoutingModule } from './route/asterisk-account-routing.module';

@NgModule({
  imports: [SharedModule, AsteriskAccountRoutingModule],
  declarations: [
    AsteriskAccountComponent,
    AsteriskAccountDetailComponent,
    AsteriskAccountUpdateComponent,
    AsteriskAccountDeleteDialogComponent,
  ],
  entryComponents: [AsteriskAccountDeleteDialogComponent],
})
export class AsteriskAccountModule {}

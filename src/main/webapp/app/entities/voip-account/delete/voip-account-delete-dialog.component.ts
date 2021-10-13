import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVoipAccount } from '../voip-account.model';
import { VoipAccountService } from '../service/voip-account.service';

@Component({
  templateUrl: './voip-account-delete-dialog.component.html',
})
export class VoipAccountDeleteDialogComponent {
  voipAccount?: IVoipAccount;

  constructor(protected voipAccountService: VoipAccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.voipAccountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

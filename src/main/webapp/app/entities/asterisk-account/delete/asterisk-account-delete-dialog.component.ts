import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAsteriskAccount } from '../asterisk-account.model';
import { AsteriskAccountService } from '../service/asterisk-account.service';

@Component({
  templateUrl: './asterisk-account-delete-dialog.component.html',
})
export class AsteriskAccountDeleteDialogComponent {
  asteriskAccount?: IAsteriskAccount;

  constructor(protected asteriskAccountService: AsteriskAccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.asteriskAccountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOptionValue } from '../option-value.model';
import { OptionValueService } from '../service/option-value.service';

@Component({
  templateUrl: './option-value-delete-dialog.component.html',
})
export class OptionValueDeleteDialogComponent {
  optionValue?: IOptionValue;

  constructor(protected optionValueService: OptionValueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.optionValueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

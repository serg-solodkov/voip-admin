import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOption } from '../option.model';
import { OptionService } from '../service/option.service';

@Component({
  templateUrl: './option-delete-dialog.component.html',
})
export class OptionDeleteDialogComponent {
  option?: IOption;

  constructor(protected optionService: OptionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.optionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

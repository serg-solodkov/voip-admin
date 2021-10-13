import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResponsiblePerson } from '../responsible-person.model';
import { ResponsiblePersonService } from '../service/responsible-person.service';

@Component({
  templateUrl: './responsible-person-delete-dialog.component.html',
})
export class ResponsiblePersonDeleteDialogComponent {
  responsiblePerson?: IResponsiblePerson;

  constructor(protected responsiblePersonService: ResponsiblePersonService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.responsiblePersonService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

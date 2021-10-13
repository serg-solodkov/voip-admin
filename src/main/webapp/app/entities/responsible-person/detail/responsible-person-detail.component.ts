import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResponsiblePerson } from '../responsible-person.model';

@Component({
  selector: 'jhi-responsible-person-detail',
  templateUrl: './responsible-person-detail.component.html',
})
export class ResponsiblePersonDetailComponent implements OnInit {
  responsiblePerson: IResponsiblePerson | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ responsiblePerson }) => {
      this.responsiblePerson = responsiblePerson;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

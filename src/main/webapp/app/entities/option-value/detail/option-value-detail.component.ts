import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOptionValue } from '../option-value.model';

@Component({
  selector: 'jhi-option-value-detail',
  templateUrl: './option-value-detail.component.html',
})
export class OptionValueDetailComponent implements OnInit {
  optionValue: IOptionValue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ optionValue }) => {
      this.optionValue = optionValue;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

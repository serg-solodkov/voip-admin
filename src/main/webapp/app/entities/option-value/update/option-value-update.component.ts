import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOptionValue, OptionValue } from '../option-value.model';
import { OptionValueService } from '../service/option-value.service';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';

@Component({
  selector: 'jhi-option-value-update',
  templateUrl: './option-value-update.component.html',
})
export class OptionValueUpdateComponent implements OnInit {
  isSaving = false;

  optionsSharedCollection: IOption[] = [];

  editForm = this.fb.group({
    id: [],
    value: [],
    option: [],
  });

  constructor(
    protected optionValueService: OptionValueService,
    protected optionService: OptionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ optionValue }) => {
      this.updateForm(optionValue);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const optionValue = this.createFromForm();
    if (optionValue.id !== undefined) {
      this.subscribeToSaveResponse(this.optionValueService.update(optionValue));
    } else {
      this.subscribeToSaveResponse(this.optionValueService.create(optionValue));
    }
  }

  trackOptionById(index: number, item: IOption): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOptionValue>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(optionValue: IOptionValue): void {
    this.editForm.patchValue({
      id: optionValue.id,
      value: optionValue.value,
      option: optionValue.option,
    });

    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing(this.optionsSharedCollection, optionValue.option);
  }

  protected loadRelationshipsOptions(): void {
    this.optionService
      .query()
      .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
      .pipe(map((options: IOption[]) => this.optionService.addOptionToCollectionIfMissing(options, this.editForm.get('option')!.value)))
      .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));
  }

  protected createFromForm(): IOptionValue {
    return {
      ...new OptionValue(),
      id: this.editForm.get(['id'])!.value,
      value: this.editForm.get(['value'])!.value,
      option: this.editForm.get(['option'])!.value,
    };
  }
}

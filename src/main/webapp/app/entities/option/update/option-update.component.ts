import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormArray, FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOption, Option } from '../option.model';
import { OptionService } from '../service/option.service';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { VendorService } from 'app/entities/vendor/service/vendor.service';
import { OptionValue } from "../../option-value/option-value.model";

@Component({
  selector: 'jhi-option-update',
  templateUrl: './option-update.component.html',
  styleUrls: ['./option-update.component.scss']
})
export class OptionUpdateComponent implements OnInit {
  isSaving = false;

  vendorsSharedCollection: IVendor[] = [];

  editForm = this.fb.group({
    id: [],
    code: [],
    descr: [],
    valueType: [],
    multiple: [],
    vendors: [],
    possibleValues: this.fb.array([])
  });

  constructor(
    protected optionService: OptionService,
    protected vendorService: VendorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  get possibleValues(): FormArray {
    return this.editForm.get('possibleValues') as FormArray;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ option }) => {
      this.updateForm(option);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const option = this.createFromForm();
    if (option.id !== undefined) {
      this.subscribeToSaveResponse(this.optionService.update(option));
    } else {
      this.subscribeToSaveResponse(this.optionService.create(option));
    }
  }

  trackVendorById(index: number, item: IVendor): number {
    return item.id!;
  }

  getSelectedVendor(option: IVendor, selectedVals?: IVendor[]): IVendor {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  addPossibleValue(): void {
    (this.editForm.get('possibleValues') as FormArray).push(
      this.fb.group({
        id: null,
        value: null,
        optionId: this.editForm.get('id')!.value,
      })
    );
  }

  removePossibleValue(index: number): void {
    (this.editForm.get('possibleValues') as FormArray).removeAt(index);
  }

  initPossibleValues(possibleValues: OptionValue[] | undefined): void {
    const possibleValuesControls = this.editForm.get('possibleValues') as FormArray;
    if (possibleValues && possibleValues.length > 0) {
      possibleValues.forEach(possibleValue => {
        possibleValuesControls.push(
          this.fb.group({
            id: possibleValue.id,
            value: possibleValue.value,
            optionId: possibleValue.option,
          })
        );
      });
    } else {
      this.addPossibleValue();
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOption>>): void {
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

  protected updateForm(option: IOption): void {
    this.editForm.patchValue({
      id: option.id,
      code: option.code,
      descr: option.descr,
      valueType: option.valueType,
      multiple: option.multiple,
      vendors: option.vendors,
    });

    this.vendorsSharedCollection = this.vendorService.addVendorToCollectionIfMissing(
      this.vendorsSharedCollection,
      ...(option.vendors ?? [])
    );
    this.initPossibleValues(option.possibleValues!);
  }

  protected loadRelationshipsOptions(): void {
    this.vendorService
      .query()
      .pipe(map((res: HttpResponse<IVendor[]>) => res.body ?? []))
      .pipe(
        map((vendors: IVendor[]) =>
          this.vendorService.addVendorToCollectionIfMissing(vendors, ...(this.editForm.get('vendors')!.value ?? []))
        )
      )
      .subscribe((vendors: IVendor[]) => (this.vendorsSharedCollection = vendors));
  }

  protected createFromForm(): IOption {
    return {
      ...new Option(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      descr: this.editForm.get(['descr'])!.value,
      valueType: this.editForm.get(['valueType'])!.value,
      multiple: this.editForm.get(['multiple'])!.value,
      vendors: this.editForm.get(['vendors'])!.value,
      possibleValues: this.editForm.get(['possibleValues']) ? this.editForm.get(['possibleValues'])!.value : [],
    };
  }
}

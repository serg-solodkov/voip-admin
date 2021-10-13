import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISetting, Setting } from '../setting.model';
import { SettingService } from '../service/setting.service';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IOptionValue } from 'app/entities/option-value/option-value.model';
import { OptionValueService } from 'app/entities/option-value/service/option-value.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';

@Component({
  selector: 'jhi-setting-update',
  templateUrl: './setting-update.component.html',
})
export class SettingUpdateComponent implements OnInit {
  isSaving = false;

  optionsSharedCollection: IOption[] = [];
  optionValuesSharedCollection: IOptionValue[] = [];
  devicesSharedCollection: IDevice[] = [];

  editForm = this.fb.group({
    id: [],
    textValue: [],
    option: [],
    selectedValues: [],
    device: [],
  });

  constructor(
    protected settingService: SettingService,
    protected optionService: OptionService,
    protected optionValueService: OptionValueService,
    protected deviceService: DeviceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ setting }) => {
      this.updateForm(setting);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const setting = this.createFromForm();
    if (setting.id !== undefined) {
      this.subscribeToSaveResponse(this.settingService.update(setting));
    } else {
      this.subscribeToSaveResponse(this.settingService.create(setting));
    }
  }

  trackOptionById(index: number, item: IOption): number {
    return item.id!;
  }

  trackOptionValueById(index: number, item: IOptionValue): number {
    return item.id!;
  }

  trackDeviceById(index: number, item: IDevice): number {
    return item.id!;
  }

  getSelectedOptionValue(option: IOptionValue, selectedVals?: IOptionValue[]): IOptionValue {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISetting>>): void {
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

  protected updateForm(setting: ISetting): void {
    this.editForm.patchValue({
      id: setting.id,
      textValue: setting.textValue,
      option: setting.option,
      selectedValues: setting.selectedValues,
      device: setting.device,
    });

    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing(this.optionsSharedCollection, setting.option);
    this.optionValuesSharedCollection = this.optionValueService.addOptionValueToCollectionIfMissing(
      this.optionValuesSharedCollection,
      ...(setting.selectedValues ?? [])
    );
    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing(this.devicesSharedCollection, setting.device);
  }

  protected loadRelationshipsOptions(): void {
    this.optionService
      .query()
      .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
      .pipe(map((options: IOption[]) => this.optionService.addOptionToCollectionIfMissing(options, this.editForm.get('option')!.value)))
      .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));

    this.optionValueService
      .query()
      .pipe(map((res: HttpResponse<IOptionValue[]>) => res.body ?? []))
      .pipe(
        map((optionValues: IOptionValue[]) =>
          this.optionValueService.addOptionValueToCollectionIfMissing(optionValues, ...(this.editForm.get('selectedValues')!.value ?? []))
        )
      )
      .subscribe((optionValues: IOptionValue[]) => (this.optionValuesSharedCollection = optionValues));

    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing(devices, this.editForm.get('device')!.value)))
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));
  }

  protected createFromForm(): ISetting {
    return {
      ...new Setting(),
      id: this.editForm.get(['id'])!.value,
      textValue: this.editForm.get(['textValue'])!.value,
      option: this.editForm.get(['option'])!.value,
      selectedValues: this.editForm.get(['selectedValues'])!.value,
      device: this.editForm.get(['device'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDeviceModel, DeviceModel } from '../device-model.model';
import { DeviceModelService } from '../service/device-model.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IOtherDeviceType } from 'app/entities/other-device-type/other-device-type.model';
import { OtherDeviceTypeService } from 'app/entities/other-device-type/service/other-device-type.service';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { VendorService } from 'app/entities/vendor/service/vendor.service';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { VendorChangeDialogComponent } from "./vendor-change-dialog/vendor-change-dialog.component";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-device-model-update',
  templateUrl: './device-model-update.component.html',
})
export class DeviceModelUpdateComponent implements OnInit {
  isSaving = false;

  otherDeviceTypesSharedCollection: IOtherDeviceType[] = [];
  vendorsSharedCollection: IVendor[] = [];
  optionsSharedCollection: IOption[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    isConfigurable: [null, [Validators.required]],
    linesCount: [null, [Validators.min(1)]],
    configTemplate: [],
    configTemplateContentType: [],
    firmwareFile: [],
    firmwareFileContentType: [],
    deviceType: [],
    otherDeviceType: [],
    vendor: [],
    options: [],
  });

  oldVendor: number | undefined;

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected deviceModelService: DeviceModelService,
    protected otherDeviceTypeService: OtherDeviceTypeService,
    protected vendorService: VendorService,
    protected optionService: OptionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceModel }) => {
      this.updateForm(deviceModel);
      this.oldVendor = deviceModel.vendor;
      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('voipadminApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deviceModel = this.createFromForm();
    if (deviceModel.id !== undefined) {
      this.subscribeToSaveResponse(this.deviceModelService.update(deviceModel));
    } else {
      this.subscribeToSaveResponse(this.deviceModelService.create(deviceModel));
    }
  }

  trackOtherDeviceTypeById(index: number, item: IOtherDeviceType): number {
    return item.id!;
  }

  trackVendorById(index: number, item: IVendor): number {
    return item.id!;
  }

  trackOptionById(index: number, item: IOption): number {
    return item.id!;
  }

  getSelectedOption(option: IOption, selectedVals?: IOption[]): IOption {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  onVendorChange(): void {
    if (this.oldVendor) {
      const modalRef = this.modalService.open(VendorChangeDialogComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.oldValue = this.oldVendor;
      modalRef.componentInstance.newValue = this.editForm.get('vendor')?.value;
      modalRef.result.then(result => {
        this.editForm.patchValue({
          vendor: result,
          options: [],
        });
        this.updateVendorOptions();
        this.oldVendor = result;
      });
    } else {
      this.oldVendor = this.editForm.get('vendor')?.value;
      this.updateVendorOptions();
    }
  }

  updateVendorOptions(): void {
    if (!this.editForm.get('vendor')?.value) {
      this.optionsSharedCollection = [];
      return;
    }
    this.optionService
      .query({'vendorsId.equals': this.editForm.get('vendor')?.value?.id})
      .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
      .pipe(
        map((options: IOption[]) =>
          this.optionService.addOptionToCollectionIfMissing(options, ...(this.editForm.get('options')!.value ?? []))
        )
      )
      .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeviceModel>>): void {
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

  protected updateForm(deviceModel: IDeviceModel): void {
    this.editForm.patchValue({
      id: deviceModel.id,
      name: deviceModel.name,
      isConfigurable: deviceModel.isConfigurable,
      linesCount: deviceModel.linesCount,
      configTemplate: deviceModel.configTemplate,
      configTemplateContentType: deviceModel.configTemplateContentType,
      firmwareFile: deviceModel.firmwareFile,
      firmwareFileContentType: deviceModel.firmwareFileContentType,
      deviceType: deviceModel.deviceType,
      otherDeviceType: deviceModel.otherDeviceType,
      vendor: deviceModel.vendor,
      options: deviceModel.options,
    });

    this.otherDeviceTypesSharedCollection = this.otherDeviceTypeService.addOtherDeviceTypeToCollectionIfMissing(
      this.otherDeviceTypesSharedCollection,
      deviceModel.otherDeviceType
    );
    this.vendorsSharedCollection = this.vendorService.addVendorToCollectionIfMissing(this.vendorsSharedCollection, deviceModel.vendor);
    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing(
      this.optionsSharedCollection,
      ...(deviceModel.options ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.otherDeviceTypeService
      .query()
      .pipe(map((res: HttpResponse<IOtherDeviceType[]>) => res.body ?? []))
      .pipe(
        map((otherDeviceTypes: IOtherDeviceType[]) =>
          this.otherDeviceTypeService.addOtherDeviceTypeToCollectionIfMissing(otherDeviceTypes, this.editForm.get('otherDeviceType')!.value)
        )
      )
      .subscribe((otherDeviceTypes: IOtherDeviceType[]) => (this.otherDeviceTypesSharedCollection = otherDeviceTypes));

    this.vendorService
      .query()
      .pipe(map((res: HttpResponse<IVendor[]>) => res.body ?? []))
      .pipe(map((vendors: IVendor[]) => this.vendorService.addVendorToCollectionIfMissing(vendors, this.editForm.get('vendor')!.value)))
      .subscribe((vendors: IVendor[]) => (this.vendorsSharedCollection = vendors));

    if (this.editForm.get('vendor')?.value) {
      this.optionService
        .query({'vendorsId.equals': this.editForm.get('vendor')?.value?.id})
        .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
        .pipe(
          map((options: IOption[]) =>
            this.optionService.addOptionToCollectionIfMissing(options, ...(this.editForm.get('options')!.value ?? []))
          )
        )
        .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));
    } else {
      this.optionsSharedCollection = [];
    }
  }

  protected createFromForm(): IDeviceModel {
    return {
      ...new DeviceModel(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      isConfigurable: this.editForm.get(['isConfigurable'])!.value,
      linesCount: this.editForm.get(['linesCount'])!.value,
      configTemplateContentType: this.editForm.get(['configTemplateContentType'])!.value,
      configTemplate: this.editForm.get(['configTemplate'])!.value,
      firmwareFileContentType: this.editForm.get(['firmwareFileContentType'])!.value,
      firmwareFile: this.editForm.get(['firmwareFile'])!.value,
      deviceType: this.editForm.get(['deviceType'])!.value,
      otherDeviceType: this.editForm.get(['otherDeviceType'])!.value,
      vendor: this.editForm.get(['vendor'])!.value,
      options: this.editForm.get(['options'])!.value,
    };
  }
}

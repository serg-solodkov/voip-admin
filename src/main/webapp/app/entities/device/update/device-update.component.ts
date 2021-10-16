import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDevice, Device } from '../device.model';
import { DeviceService } from '../service/device.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IDeviceModel } from 'app/entities/device-model/device-model.model';
import { DeviceModelService } from 'app/entities/device-model/service/device-model.service';
import { IResponsiblePerson } from 'app/entities/responsible-person/responsible-person.model';
import { ResponsiblePersonService } from 'app/entities/responsible-person/service/responsible-person.service';
import { ValidationService } from "../../../shared/service/validation.service";

@Component({
  selector: 'jhi-device-update',
  styleUrls: ['./device-update.component.scss'],
  templateUrl: './device-update.component.html',
})
export class DeviceUpdateComponent implements OnInit {
  isSaving = false;

  devicesSharedCollection: IDevice[] = [];
  deviceModelsSharedCollection: IDeviceModel[] = [];
  responsiblePeopleSharedCollection: IResponsiblePerson[] = [];

  editForm = this.fb.group({
    id: [],
    mac: [null, [Validators.required, Validators.pattern('^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$')]],
    inventoryNumber: [],
    location: [],
    hostname: [],
    webLogin: [],
    webPassword: [],
    dhcpEnabled: [],
    ipAddress: [null, [Validators.pattern('^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$')]],
    subnetMask: [
      null,
      [
        Validators.pattern(
          '^(((255\\.){3}(255|254|252|248|240|224|192|128|0+))|((255\\.){2}(255|254|252|248|240|224|192|128|0+)\\.0)|((255\\.)(255|254|252|248|240|224|192|128|0+)(\\.0+){2})|((255|254|252|248|240|224|192|128|0+)(\\.0+){3}))$'
        ),
      ],
    ],
    defaultGw: [null, this.validationService.ipAddressOrDomainNamePattern],
    dns1: [null, this.validationService.ipAddressOrDomainNamePattern],
    dns2: [null, this.validationService.ipAddressOrDomainNamePattern],
    provisioningMode: [],
    provisioningUrl: [],
    ntpServer: [],
    notes: [],
    configuration: [],
    configurationContentType: [],
    model: [],
    responsiblePerson: [],
    parent: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected deviceService: DeviceService,
    protected deviceModelService: DeviceModelService,
    protected responsiblePersonService: ResponsiblePersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected validationService: ValidationService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ device }) => {
      this.updateForm(device);

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
    const device = this.createFromForm();
    if (device.id) {
      this.subscribeToSaveResponse(this.deviceService.update(device));
    } else {
      this.subscribeToSaveResponse(this.deviceService.create(device));
    }
  }

  trackDeviceById(index: number, item: IDevice): number {
    return item.id!;
  }

  trackDeviceModelById(index: number, item: IDeviceModel): number {
    return item.id!;
  }

  trackResponsiblePersonById(index: number, item: IResponsiblePerson): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevice>>): void {
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

  protected updateForm(device: IDevice): void {
    this.editForm.patchValue({
      id: device.id,
      mac: device.mac,
      inventoryNumber: device.inventoryNumber,
      location: device.location,
      hostname: device.hostname,
      webLogin: device.webLogin,
      webPassword: device.webPassword,
      dhcpEnabled: device.dhcpEnabled,
      ipAddress: device.ipAddress,
      subnetMask: device.subnetMask,
      defaultGw: device.defaultGw,
      dns1: device.dns1,
      dns2: device.dns2,
      provisioningMode: device.provisioningMode,
      provisioningUrl: device.provisioningUrl,
      ntpServer: device.ntpServer,
      notes: device.notes,
      configuration: device.configuration,
      configurationContentType: device.configurationContentType,
      model: device.model,
      responsiblePerson: device.responsiblePerson,
      parent: device.parent,
    });

    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing(this.devicesSharedCollection, device.parent);
    this.deviceModelsSharedCollection = this.deviceModelService.addDeviceModelToCollectionIfMissing(
      this.deviceModelsSharedCollection,
      device.model
    );
    this.responsiblePeopleSharedCollection = this.responsiblePersonService.addResponsiblePersonToCollectionIfMissing(
      this.responsiblePeopleSharedCollection,
      device.responsiblePerson
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing(devices, this.editForm.get('parent')!.value)))
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));

    this.deviceModelService
      .query()
      .pipe(map((res: HttpResponse<IDeviceModel[]>) => res.body ?? []))
      .pipe(
        map((deviceModels: IDeviceModel[]) =>
          this.deviceModelService.addDeviceModelToCollectionIfMissing(deviceModels, this.editForm.get('model')!.value)
        )
      )
      .subscribe((deviceModels: IDeviceModel[]) => (this.deviceModelsSharedCollection = deviceModels));

    this.responsiblePersonService
      .query()
      .pipe(map((res: HttpResponse<IResponsiblePerson[]>) => res.body ?? []))
      .pipe(
        map((responsiblePeople: IResponsiblePerson[]) =>
          this.responsiblePersonService.addResponsiblePersonToCollectionIfMissing(
            responsiblePeople,
            this.editForm.get('responsiblePerson')!.value
          )
        )
      )
      .subscribe((responsiblePeople: IResponsiblePerson[]) => (this.responsiblePeopleSharedCollection = responsiblePeople));
  }

  protected createFromForm(): IDevice {
    return {
      ...new Device(),
      id: this.editForm.get(['id'])!.value,
      mac: this.editForm.get(['mac'])!.value,
      inventoryNumber: this.editForm.get(['inventoryNumber'])!.value,
      location: this.editForm.get(['location'])!.value,
      hostname: this.editForm.get(['hostname'])!.value,
      webLogin: this.editForm.get(['webLogin'])!.value,
      webPassword: this.editForm.get(['webPassword'])!.value,
      dhcpEnabled: this.editForm.get(['dhcpEnabled'])!.value,
      ipAddress: this.editForm.get(['ipAddress'])!.value,
      subnetMask: this.editForm.get(['subnetMask'])!.value,
      defaultGw: this.editForm.get(['defaultGw'])!.value,
      dns1: this.editForm.get(['dns1'])!.value,
      dns2: this.editForm.get(['dns2'])!.value,
      provisioningMode: this.editForm.get(['provisioningMode'])!.value,
      provisioningUrl: this.editForm.get(['provisioningUrl'])!.value,
      ntpServer: this.editForm.get(['ntpServer'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      configurationContentType: this.editForm.get(['configurationContentType'])!.value,
      configuration: this.editForm.get(['configuration'])!.value,
      model: this.editForm.get(['model'])!.value,
      responsiblePerson: this.editForm.get(['responsiblePerson'])!.value,
      parent: this.editForm.get(['parent'])!.value,
    };
  }
}

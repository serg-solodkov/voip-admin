import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { AbstractControl, FormArray, FormBuilder, Validators } from '@angular/forms';
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
import { IVoipAccount } from "../../voip-account/voip-account.model";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { DeviceModelChangeDialogComponent } from "./device-model-change-dialog/device-model-change-dialog.component";
import { IOption } from "../../option/option.model";
import { OptionService } from "../../option/service/option.service";
import { ISetting, Setting } from "../../setting/setting.model";
import { IOptionValue } from "../../option-value/option-value.model";

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
  optionsSharedCollection: IOption[] = [];
  settingPossibleValues: IOptionValue[][] = [];

  oldModelValue: IDeviceModel | null = null;

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
    voipAccounts: this.fb.array([]),
    settings: this.fb.array([]),
    enableSingleSipServer: [true],
    singleSipServer: [],
    singleSipPort: [],
  });

  get voipAccounts(): FormArray {
    return this.editForm.get('voipAccounts') as FormArray;
  }

  get settings(): FormArray {
    return this.editForm.get('settings') as FormArray;
  }

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected deviceService: DeviceService,
    protected deviceModelService: DeviceModelService,
    protected responsiblePersonService: ResponsiblePersonService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected validationService: ValidationService,
    protected modalService: NgbModal,
    protected optionService: OptionService
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
    this.beforeSave();
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

  onDeviceModelChange(): void {
    if (this.oldModelValue) {
      const modalRef = this.modalService.open(DeviceModelChangeDialogComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.oldValue = this.oldModelValue;
      modalRef.componentInstance.newValue = this.editForm.get('model')?.value;
      modalRef.result.then(result => {
        this.editForm.patchValue({
          model: result,
        });
        if (result !== this.oldModelValue) {
          this.voipAccounts.clear();
          this.initVoipAccounts();
          this.settings.clear();
          this.initSettings([]);
          this.loadAvailableOptions();
        }
        this.oldModelValue = result;
      });
    } else {
      this.oldModelValue = this.editForm.get('model')?.value;
      this.initVoipAccounts();
      this.initSettings([]);
      this.loadAvailableOptions();
    }
  }

  initVoipAccounts(voipAccounts?: IVoipAccount[]): void {
    if (!this.editForm.get('model')?.value) {
      this.voipAccounts.clear();
      return;
    }

    const deviceModel = this.deviceModelsSharedCollection.find(model => model.id === this.editForm.get('model')?.value?.id);
    if (deviceModel?.isConfigurable && deviceModel.linesCount! > 0) {
      if (voipAccounts && voipAccounts.length > 0) {
        voipAccounts
          .sort((account1, account2) => account1.lineNumber! - account2.lineNumber!)
          .forEach(account => this.addVoipAccount(account));

        const sipServers = new Set(voipAccounts.filter(account => account.sipServer).map(account => account.sipServer));
        const sipPorts = new Set(voipAccounts.filter(account => account.sipPort).map(account => account.sipPort));
        this.editForm.patchValue({
          enableSingleSipServer: sipServers.size === 1 && sipPorts.size === 1,
          singleSipServer: sipServers.size === 1 && sipPorts.size === 1 ? sipServers.values().next().value : null,
          singleSipPort: sipServers.size === 1 && sipPorts.size === 1 ? sipPorts.values().next().value : null,
        });
      } else {
        for (let i = 0; i < deviceModel.linesCount!; i++) {
          this.addVoipAccount();
        }
        this.editForm.patchValue({
          enableSingleSipServer: true,
          singleSipServer: null,
          singleSipPort: null,
        });
      }
    }
  }

  addVoipAccount(voipAccount?: IVoipAccount): void {
    this.voipAccounts.push(
      this.fb.group({
        id: [voipAccount ? voipAccount.id : null],
        manuallyCreated: [true],
        username: [voipAccount ? voipAccount.username : null],
        password: [voipAccount ? voipAccount.password : null],
        sipServer: [voipAccount ? voipAccount.sipServer : null],
        sipPort: [voipAccount ? voipAccount.sipPort : null],
        lineEnable: [voipAccount ? voipAccount.lineEnable : false],
        lineNumber: [voipAccount ? voipAccount.lineNumber : this.voipAccounts.length + 1],
        deviceId: [this.editForm.get('id')!.value],
      })
    );
  }

  clearLineSetting(index: number): void {
    this.voipAccounts
      .get(index.toString())
      ?.patchValue({
        id: null,
        manuallyCreated: true,
        username: null,
        password: null,
        sipServer: null,
        sipPort: null,
        deviceId: this.editForm.get('id')!.value,
      });
  }

  beforeSave(): void {
    this.handleVoipAccountsSingleSipServer();
  }

  handleVoipAccountsSingleSipServer(): void {
    if (this.voipAccounts.length === 0 || !this.editForm.get('enableSingleSipServer')?.value
      || !this.editForm.get('singleSipServer')?.value || !this.editForm.get('singleSipPort')?.value) {
      return;
    }
    for (let index = 0; index < this.voipAccounts.length; index++) {
      this.voipAccounts.get(index.toString())?.patchValue({
        sipServer: this.editForm.get('singleSipServer')?.value,
        sipPort: this.editForm.get('singleSipPort')?.value
      });
    }
  }

  initSettings(settings: ISetting[] | undefined): void {
    const settingsControls = this.editForm.get('settings') as FormArray;
    if (settings && settings.length > 0) {
      settings.forEach((setting: ISetting, index: number) => {
        if (setting.option) {
          this.addPossibleValuesForOption(setting.option, index);
        }
        settingsControls.push(
          this.fb.group({
            id: [setting.id],
            textValue: [setting.textValue],
            selectedValues: [
              setting.option?.multiple ? setting.selectedValues : setting.selectedValues ? setting.selectedValues[0] : null,
            ],
            option: [setting.option]
          })
        );
      });
    } else {
      this.addSetting();
    }
  }

  onSettingOptionChange(option: IOption, index: number): void {
    this.addPossibleValuesForOption(option, index);
  }

  addSetting(): void {
    this.settings.push(
      this.fb.group({
        id: [],
        textValue: [],
        selectedValues: [[]],
        option: [],
        deviceId: [this.editForm.get('id')!.value],
      })
    );
  }

  removeSetting(index: number): void {
    this.settings.removeAt(index);
  }

  addPossibleValuesForOption(option: IOption, arrayIndex: number): void {
    if (option.possibleValues) {
      this.settingPossibleValues[arrayIndex] = option.possibleValues;
    }
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
    const alreadySetOptions = this.editForm.get('settings')!.value.map((setting: ISetting) => setting.option);
    this.optionsSharedCollection = this.optionService.addOptionToCollectionIfMissing(
      this.optionsSharedCollection, ...(alreadySetOptions ?? [])
    );
    if (device.model) {
      this.oldModelValue = device.model;
      this.initVoipAccounts(device.voipAccounts!);
      this.initSettings(device.settings!);
    }
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
    this.loadAvailableOptions();
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
      voipAccounts: this.editForm.get(['voipAccounts'])!.value,
      settings: this.settings
        .controls.map(settingControl => this.createSettingFromForm(settingControl))
        .filter(setting => setting.option && (setting.textValue || (setting.selectedValues && setting.selectedValues.length > 0))),
    };
  }

  private createSettingFromForm(settingControl: AbstractControl): ISetting {
    return {
      ...new Setting(),
      id: settingControl.get('id')?.value,
      option: settingControl.get('option')?.value,
      textValue: settingControl.get('textValue')?.value,
      selectedValues:
        settingControl.get('option')?.value?.valueType === 'SELECT'
          ? settingControl.get('option')?.value?.multiple
          ? settingControl.get('selectedValues')?.value
          : [settingControl.get('selectedValues')?.value]
          : null,
    };
  }

  private loadAvailableOptions(): void {
    if (this.editForm.get('model')?.value) {
      // Load options available for selected device model
      this.optionService
        .query({'modelsId.equals': this.editForm.get('model')?.value?.id})
        .pipe(map((res: HttpResponse<IOption[]>) => res.body ?? []))
        .pipe(
          map((options: IOption[]) => {
            const alreadySetOptions = this.editForm.get('settings')!.value.map((setting: ISetting) => setting.option);
            this.optionService.addOptionToCollectionIfMissing(
              options,
              ...(alreadySetOptions ?? [])
            );
            return options;
          })
        )
        .subscribe((options: IOption[]) => (this.optionsSharedCollection = options));
    } else {
      this.optionsSharedCollection = [];
    }
  }
}

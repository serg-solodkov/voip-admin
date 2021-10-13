import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVoipAccount, VoipAccount } from '../voip-account.model';
import { VoipAccountService } from '../service/voip-account.service';
import { IAsteriskAccount } from 'app/entities/asterisk-account/asterisk-account.model';
import { AsteriskAccountService } from 'app/entities/asterisk-account/service/asterisk-account.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';

@Component({
  selector: 'jhi-voip-account-update',
  templateUrl: './voip-account-update.component.html',
})
export class VoipAccountUpdateComponent implements OnInit {
  isSaving = false;

  asteriskAccountsCollection: IAsteriskAccount[] = [];
  devicesSharedCollection: IDevice[] = [];

  editForm = this.fb.group({
    id: [],
    manuallyCreated: [],
    username: [null, [Validators.required]],
    password: [],
    sipServer: [],
    sipPort: [],
    lineEnable: [],
    lineNumber: [],
    asteriskAccount: [],
    device: [],
  });

  constructor(
    protected voipAccountService: VoipAccountService,
    protected asteriskAccountService: AsteriskAccountService,
    protected deviceService: DeviceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ voipAccount }) => {
      this.updateForm(voipAccount);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const voipAccount = this.createFromForm();
    if (voipAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.voipAccountService.update(voipAccount));
    } else {
      this.subscribeToSaveResponse(this.voipAccountService.create(voipAccount));
    }
  }

  trackAsteriskAccountById(index: number, item: IAsteriskAccount): number {
    return item.id!;
  }

  trackDeviceById(index: number, item: IDevice): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVoipAccount>>): void {
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

  protected updateForm(voipAccount: IVoipAccount): void {
    this.editForm.patchValue({
      id: voipAccount.id,
      manuallyCreated: voipAccount.manuallyCreated,
      username: voipAccount.username,
      password: voipAccount.password,
      sipServer: voipAccount.sipServer,
      sipPort: voipAccount.sipPort,
      lineEnable: voipAccount.lineEnable,
      lineNumber: voipAccount.lineNumber,
      asteriskAccount: voipAccount.asteriskAccount,
      device: voipAccount.device,
    });

    this.asteriskAccountsCollection = this.asteriskAccountService.addAsteriskAccountToCollectionIfMissing(
      this.asteriskAccountsCollection,
      voipAccount.asteriskAccount
    );
    this.devicesSharedCollection = this.deviceService.addDeviceToCollectionIfMissing(this.devicesSharedCollection, voipAccount.device);
  }

  protected loadRelationshipsOptions(): void {
    this.asteriskAccountService
      .query({ filter: 'voipaccount-is-null' })
      .pipe(map((res: HttpResponse<IAsteriskAccount[]>) => res.body ?? []))
      .pipe(
        map((asteriskAccounts: IAsteriskAccount[]) =>
          this.asteriskAccountService.addAsteriskAccountToCollectionIfMissing(asteriskAccounts, this.editForm.get('asteriskAccount')!.value)
        )
      )
      .subscribe((asteriskAccounts: IAsteriskAccount[]) => (this.asteriskAccountsCollection = asteriskAccounts));

    this.deviceService
      .query()
      .pipe(map((res: HttpResponse<IDevice[]>) => res.body ?? []))
      .pipe(map((devices: IDevice[]) => this.deviceService.addDeviceToCollectionIfMissing(devices, this.editForm.get('device')!.value)))
      .subscribe((devices: IDevice[]) => (this.devicesSharedCollection = devices));
  }

  protected createFromForm(): IVoipAccount {
    return {
      ...new VoipAccount(),
      id: this.editForm.get(['id'])!.value,
      manuallyCreated: this.editForm.get(['manuallyCreated'])!.value,
      username: this.editForm.get(['username'])!.value,
      password: this.editForm.get(['password'])!.value,
      sipServer: this.editForm.get(['sipServer'])!.value,
      sipPort: this.editForm.get(['sipPort'])!.value,
      lineEnable: this.editForm.get(['lineEnable'])!.value,
      lineNumber: this.editForm.get(['lineNumber'])!.value,
      asteriskAccount: this.editForm.get(['asteriskAccount'])!.value,
      device: this.editForm.get(['device'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAsteriskAccount, AsteriskAccount } from '../asterisk-account.model';
import { AsteriskAccountService } from '../service/asterisk-account.service';

@Component({
  selector: 'jhi-asterisk-account-update',
  templateUrl: './asterisk-account-update.component.html',
})
export class AsteriskAccountUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    username: [],
    asteriskId: [],
  });

  constructor(
    protected asteriskAccountService: AsteriskAccountService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ asteriskAccount }) => {
      this.updateForm(asteriskAccount);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const asteriskAccount = this.createFromForm();
    if (asteriskAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.asteriskAccountService.update(asteriskAccount));
    } else {
      this.subscribeToSaveResponse(this.asteriskAccountService.create(asteriskAccount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAsteriskAccount>>): void {
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

  protected updateForm(asteriskAccount: IAsteriskAccount): void {
    this.editForm.patchValue({
      id: asteriskAccount.id,
      username: asteriskAccount.username,
      asteriskId: asteriskAccount.asteriskId,
    });
  }

  protected createFromForm(): IAsteriskAccount {
    return {
      ...new AsteriskAccount(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
      asteriskId: this.editForm.get(['asteriskId'])!.value,
    };
  }
}

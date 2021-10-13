import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOtherDeviceType, OtherDeviceType } from '../other-device-type.model';
import { OtherDeviceTypeService } from '../service/other-device-type.service';

@Component({
  selector: 'jhi-other-device-type-update',
  templateUrl: './other-device-type-update.component.html',
})
export class OtherDeviceTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
  });

  constructor(
    protected otherDeviceTypeService: OtherDeviceTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ otherDeviceType }) => {
      this.updateForm(otherDeviceType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const otherDeviceType = this.createFromForm();
    if (otherDeviceType.id !== undefined) {
      this.subscribeToSaveResponse(this.otherDeviceTypeService.update(otherDeviceType));
    } else {
      this.subscribeToSaveResponse(this.otherDeviceTypeService.create(otherDeviceType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOtherDeviceType>>): void {
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

  protected updateForm(otherDeviceType: IOtherDeviceType): void {
    this.editForm.patchValue({
      id: otherDeviceType.id,
      name: otherDeviceType.name,
      description: otherDeviceType.description,
    });
  }

  protected createFromForm(): IOtherDeviceType {
    return {
      ...new OtherDeviceType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}

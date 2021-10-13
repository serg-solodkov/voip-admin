import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVendor, Vendor } from '../vendor.model';
import { VendorService } from '../service/vendor.service';

@Component({
  selector: 'jhi-vendor-update',
  templateUrl: './vendor-update.component.html',
})
export class VendorUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
  });

  constructor(protected vendorService: VendorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendor }) => {
      this.updateForm(vendor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vendor = this.createFromForm();
    if (vendor.id !== undefined) {
      this.subscribeToSaveResponse(this.vendorService.update(vendor));
    } else {
      this.subscribeToSaveResponse(this.vendorService.create(vendor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVendor>>): void {
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

  protected updateForm(vendor: IVendor): void {
    this.editForm.patchValue({
      id: vendor.id,
      name: vendor.name,
    });
  }

  protected createFromForm(): IVendor {
    return {
      ...new Vendor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}

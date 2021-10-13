import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IResponsiblePerson, ResponsiblePerson } from '../responsible-person.model';
import { ResponsiblePersonService } from '../service/responsible-person.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

@Component({
  selector: 'jhi-responsible-person-update',
  templateUrl: './responsible-person-update.component.html',
})
export class ResponsiblePersonUpdateComponent implements OnInit {
  isSaving = false;

  departmentsSharedCollection: IDepartment[] = [];

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    firstName: [null, [Validators.required]],
    secondName: [],
    lastName: [null, [Validators.required]],
    position: [],
    room: [],
    department: [],
  });

  constructor(
    protected responsiblePersonService: ResponsiblePersonService,
    protected departmentService: DepartmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ responsiblePerson }) => {
      this.updateForm(responsiblePerson);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const responsiblePerson = this.createFromForm();
    if (responsiblePerson.id !== undefined) {
      this.subscribeToSaveResponse(this.responsiblePersonService.update(responsiblePerson));
    } else {
      this.subscribeToSaveResponse(this.responsiblePersonService.create(responsiblePerson));
    }
  }

  trackDepartmentById(index: number, item: IDepartment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResponsiblePerson>>): void {
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

  protected updateForm(responsiblePerson: IResponsiblePerson): void {
    this.editForm.patchValue({
      id: responsiblePerson.id,
      code: responsiblePerson.code,
      firstName: responsiblePerson.firstName,
      secondName: responsiblePerson.secondName,
      lastName: responsiblePerson.lastName,
      position: responsiblePerson.position,
      room: responsiblePerson.room,
      department: responsiblePerson.department,
    });

    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing(
      this.departmentsSharedCollection,
      responsiblePerson.department
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing(departments, this.editForm.get('department')!.value)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));
  }

  protected createFromForm(): IResponsiblePerson {
    return {
      ...new ResponsiblePerson(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      secondName: this.editForm.get(['secondName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      position: this.editForm.get(['position'])!.value,
      room: this.editForm.get(['room'])!.value,
      department: this.editForm.get(['department'])!.value,
    };
  }
}

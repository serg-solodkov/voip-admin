jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ResponsiblePersonService } from '../service/responsible-person.service';
import { IResponsiblePerson, ResponsiblePerson } from '../responsible-person.model';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

import { ResponsiblePersonUpdateComponent } from './responsible-person-update.component';

describe('Component Tests', () => {
  describe('ResponsiblePerson Management Update Component', () => {
    let comp: ResponsiblePersonUpdateComponent;
    let fixture: ComponentFixture<ResponsiblePersonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let responsiblePersonService: ResponsiblePersonService;
    let departmentService: DepartmentService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ResponsiblePersonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ResponsiblePersonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResponsiblePersonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      responsiblePersonService = TestBed.inject(ResponsiblePersonService);
      departmentService = TestBed.inject(DepartmentService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Department query and add missing value', () => {
        const responsiblePerson: IResponsiblePerson = { id: 456 };
        const department: IDepartment = { id: 58387 };
        responsiblePerson.department = department;

        const departmentCollection: IDepartment[] = [{ id: 60012 }];
        jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
        const additionalDepartments = [department];
        const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
        jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ responsiblePerson });
        comp.ngOnInit();

        expect(departmentService.query).toHaveBeenCalled();
        expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(departmentCollection, ...additionalDepartments);
        expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const responsiblePerson: IResponsiblePerson = { id: 456 };
        const department: IDepartment = { id: 3847 };
        responsiblePerson.department = department;

        activatedRoute.data = of({ responsiblePerson });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(responsiblePerson));
        expect(comp.departmentsSharedCollection).toContain(department);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ResponsiblePerson>>();
        const responsiblePerson = { id: 123 };
        jest.spyOn(responsiblePersonService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ responsiblePerson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: responsiblePerson }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(responsiblePersonService.update).toHaveBeenCalledWith(responsiblePerson);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ResponsiblePerson>>();
        const responsiblePerson = new ResponsiblePerson();
        jest.spyOn(responsiblePersonService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ responsiblePerson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: responsiblePerson }));
        saveSubject.complete();

        // THEN
        expect(responsiblePersonService.create).toHaveBeenCalledWith(responsiblePerson);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ResponsiblePerson>>();
        const responsiblePerson = { id: 123 };
        jest.spyOn(responsiblePersonService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ responsiblePerson });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(responsiblePersonService.update).toHaveBeenCalledWith(responsiblePerson);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackDepartmentById', () => {
        it('Should return tracked Department primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDepartmentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

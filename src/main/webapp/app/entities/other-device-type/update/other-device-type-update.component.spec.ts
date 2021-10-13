jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OtherDeviceTypeService } from '../service/other-device-type.service';
import { IOtherDeviceType, OtherDeviceType } from '../other-device-type.model';

import { OtherDeviceTypeUpdateComponent } from './other-device-type-update.component';

describe('Component Tests', () => {
  describe('OtherDeviceType Management Update Component', () => {
    let comp: OtherDeviceTypeUpdateComponent;
    let fixture: ComponentFixture<OtherDeviceTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let otherDeviceTypeService: OtherDeviceTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OtherDeviceTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OtherDeviceTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OtherDeviceTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      otherDeviceTypeService = TestBed.inject(OtherDeviceTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const otherDeviceType: IOtherDeviceType = { id: 456 };

        activatedRoute.data = of({ otherDeviceType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(otherDeviceType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OtherDeviceType>>();
        const otherDeviceType = { id: 123 };
        jest.spyOn(otherDeviceTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ otherDeviceType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: otherDeviceType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(otherDeviceTypeService.update).toHaveBeenCalledWith(otherDeviceType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OtherDeviceType>>();
        const otherDeviceType = new OtherDeviceType();
        jest.spyOn(otherDeviceTypeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ otherDeviceType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: otherDeviceType }));
        saveSubject.complete();

        // THEN
        expect(otherDeviceTypeService.create).toHaveBeenCalledWith(otherDeviceType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OtherDeviceType>>();
        const otherDeviceType = { id: 123 };
        jest.spyOn(otherDeviceTypeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ otherDeviceType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(otherDeviceTypeService.update).toHaveBeenCalledWith(otherDeviceType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

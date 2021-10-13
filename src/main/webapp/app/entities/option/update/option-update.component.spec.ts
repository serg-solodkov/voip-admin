jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OptionService } from '../service/option.service';
import { IOption, Option } from '../option.model';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { VendorService } from 'app/entities/vendor/service/vendor.service';

import { OptionUpdateComponent } from './option-update.component';

describe('Component Tests', () => {
  describe('Option Management Update Component', () => {
    let comp: OptionUpdateComponent;
    let fixture: ComponentFixture<OptionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let optionService: OptionService;
    let vendorService: VendorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OptionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OptionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OptionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      optionService = TestBed.inject(OptionService);
      vendorService = TestBed.inject(VendorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Vendor query and add missing value', () => {
        const option: IOption = { id: 456 };
        const vendors: IVendor[] = [{ id: 93405 }];
        option.vendors = vendors;

        const vendorCollection: IVendor[] = [{ id: 8435 }];
        jest.spyOn(vendorService, 'query').mockReturnValue(of(new HttpResponse({ body: vendorCollection })));
        const additionalVendors = [...vendors];
        const expectedCollection: IVendor[] = [...additionalVendors, ...vendorCollection];
        jest.spyOn(vendorService, 'addVendorToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ option });
        comp.ngOnInit();

        expect(vendorService.query).toHaveBeenCalled();
        expect(vendorService.addVendorToCollectionIfMissing).toHaveBeenCalledWith(vendorCollection, ...additionalVendors);
        expect(comp.vendorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const option: IOption = { id: 456 };
        const vendors: IVendor = { id: 10075 };
        option.vendors = [vendors];

        activatedRoute.data = of({ option });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(option));
        expect(comp.vendorsSharedCollection).toContain(vendors);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Option>>();
        const option = { id: 123 };
        jest.spyOn(optionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ option });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: option }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(optionService.update).toHaveBeenCalledWith(option);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Option>>();
        const option = new Option();
        jest.spyOn(optionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ option });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: option }));
        saveSubject.complete();

        // THEN
        expect(optionService.create).toHaveBeenCalledWith(option);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Option>>();
        const option = { id: 123 };
        jest.spyOn(optionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ option });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(optionService.update).toHaveBeenCalledWith(option);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVendorById', () => {
        it('Should return tracked Vendor primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVendorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedVendor', () => {
        it('Should return option if no Vendor is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedVendor(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Vendor for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedVendor(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Vendor is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedVendor(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});

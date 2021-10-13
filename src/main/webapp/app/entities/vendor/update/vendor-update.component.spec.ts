jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VendorService } from '../service/vendor.service';
import { IVendor, Vendor } from '../vendor.model';

import { VendorUpdateComponent } from './vendor-update.component';

describe('Component Tests', () => {
  describe('Vendor Management Update Component', () => {
    let comp: VendorUpdateComponent;
    let fixture: ComponentFixture<VendorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vendorService: VendorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VendorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VendorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VendorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vendorService = TestBed.inject(VendorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const vendor: IVendor = { id: 456 };

        activatedRoute.data = of({ vendor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vendor));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vendor>>();
        const vendor = { id: 123 };
        jest.spyOn(vendorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vendor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vendor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(vendorService.update).toHaveBeenCalledWith(vendor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vendor>>();
        const vendor = new Vendor();
        jest.spyOn(vendorService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vendor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vendor }));
        saveSubject.complete();

        // THEN
        expect(vendorService.create).toHaveBeenCalledWith(vendor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vendor>>();
        const vendor = { id: 123 };
        jest.spyOn(vendorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vendor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(vendorService.update).toHaveBeenCalledWith(vendor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

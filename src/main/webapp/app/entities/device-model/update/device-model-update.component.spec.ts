jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DeviceModelService } from '../service/device-model.service';
import { IDeviceModel, DeviceModel } from '../device-model.model';
import { IOtherDeviceType } from 'app/entities/other-device-type/other-device-type.model';
import { OtherDeviceTypeService } from 'app/entities/other-device-type/service/other-device-type.service';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { VendorService } from 'app/entities/vendor/service/vendor.service';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';

import { DeviceModelUpdateComponent } from './device-model-update.component';

describe('Component Tests', () => {
  describe('DeviceModel Management Update Component', () => {
    let comp: DeviceModelUpdateComponent;
    let fixture: ComponentFixture<DeviceModelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let deviceModelService: DeviceModelService;
    let otherDeviceTypeService: OtherDeviceTypeService;
    let vendorService: VendorService;
    let optionService: OptionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DeviceModelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DeviceModelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeviceModelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      deviceModelService = TestBed.inject(DeviceModelService);
      otherDeviceTypeService = TestBed.inject(OtherDeviceTypeService);
      vendorService = TestBed.inject(VendorService);
      optionService = TestBed.inject(OptionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call OtherDeviceType query and add missing value', () => {
        const deviceModel: IDeviceModel = { id: 456 };
        const otherDeviceType: IOtherDeviceType = { id: 7377 };
        deviceModel.otherDeviceType = otherDeviceType;

        const otherDeviceTypeCollection: IOtherDeviceType[] = [{ id: 8252 }];
        jest.spyOn(otherDeviceTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: otherDeviceTypeCollection })));
        const additionalOtherDeviceTypes = [otherDeviceType];
        const expectedCollection: IOtherDeviceType[] = [...additionalOtherDeviceTypes, ...otherDeviceTypeCollection];
        jest.spyOn(otherDeviceTypeService, 'addOtherDeviceTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ deviceModel });
        comp.ngOnInit();

        expect(otherDeviceTypeService.query).toHaveBeenCalled();
        expect(otherDeviceTypeService.addOtherDeviceTypeToCollectionIfMissing).toHaveBeenCalledWith(
          otherDeviceTypeCollection,
          ...additionalOtherDeviceTypes
        );
        expect(comp.otherDeviceTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Vendor query and add missing value', () => {
        const deviceModel: IDeviceModel = { id: 456 };
        const vendor: IVendor = { id: 41797 };
        deviceModel.vendor = vendor;

        const vendorCollection: IVendor[] = [{ id: 65445 }];
        jest.spyOn(vendorService, 'query').mockReturnValue(of(new HttpResponse({ body: vendorCollection })));
        const additionalVendors = [vendor];
        const expectedCollection: IVendor[] = [...additionalVendors, ...vendorCollection];
        jest.spyOn(vendorService, 'addVendorToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ deviceModel });
        comp.ngOnInit();

        expect(vendorService.query).toHaveBeenCalled();
        expect(vendorService.addVendorToCollectionIfMissing).toHaveBeenCalledWith(vendorCollection, ...additionalVendors);
        expect(comp.vendorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Option query and add missing value', () => {
        const deviceModel: IDeviceModel = { id: 456 };
        const options: IOption[] = [{ id: 87157 }];
        deviceModel.options = options;

        const optionCollection: IOption[] = [{ id: 25050 }];
        jest.spyOn(optionService, 'query').mockReturnValue(of(new HttpResponse({ body: optionCollection })));
        const additionalOptions = [...options];
        const expectedCollection: IOption[] = [...additionalOptions, ...optionCollection];
        jest.spyOn(optionService, 'addOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ deviceModel });
        comp.ngOnInit();

        expect(optionService.query).toHaveBeenCalled();
        expect(optionService.addOptionToCollectionIfMissing).toHaveBeenCalledWith(optionCollection, ...additionalOptions);
        expect(comp.optionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const deviceModel: IDeviceModel = { id: 456 };
        const otherDeviceType: IOtherDeviceType = { id: 90294 };
        deviceModel.otherDeviceType = otherDeviceType;
        const vendor: IVendor = { id: 8899 };
        deviceModel.vendor = vendor;
        const options: IOption = { id: 24083 };
        deviceModel.options = [options];

        activatedRoute.data = of({ deviceModel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(deviceModel));
        expect(comp.otherDeviceTypesSharedCollection).toContain(otherDeviceType);
        expect(comp.vendorsSharedCollection).toContain(vendor);
        expect(comp.optionsSharedCollection).toContain(options);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DeviceModel>>();
        const deviceModel = { id: 123 };
        jest.spyOn(deviceModelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ deviceModel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: deviceModel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(deviceModelService.update).toHaveBeenCalledWith(deviceModel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DeviceModel>>();
        const deviceModel = new DeviceModel();
        jest.spyOn(deviceModelService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ deviceModel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: deviceModel }));
        saveSubject.complete();

        // THEN
        expect(deviceModelService.create).toHaveBeenCalledWith(deviceModel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<DeviceModel>>();
        const deviceModel = { id: 123 };
        jest.spyOn(deviceModelService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ deviceModel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(deviceModelService.update).toHaveBeenCalledWith(deviceModel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOtherDeviceTypeById', () => {
        it('Should return tracked OtherDeviceType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOtherDeviceTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackVendorById', () => {
        it('Should return tracked Vendor primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVendorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackOptionById', () => {
        it('Should return tracked Option primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOptionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedOption', () => {
        it('Should return option if no Option is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedOption(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Option for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedOption(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Option is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedOption(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});

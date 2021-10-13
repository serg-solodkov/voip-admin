jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SettingService } from '../service/setting.service';
import { ISetting, Setting } from '../setting.model';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';
import { IOptionValue } from 'app/entities/option-value/option-value.model';
import { OptionValueService } from 'app/entities/option-value/service/option-value.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';

import { SettingUpdateComponent } from './setting-update.component';

describe('Component Tests', () => {
  describe('Setting Management Update Component', () => {
    let comp: SettingUpdateComponent;
    let fixture: ComponentFixture<SettingUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let settingService: SettingService;
    let optionService: OptionService;
    let optionValueService: OptionValueService;
    let deviceService: DeviceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SettingUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SettingUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SettingUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      settingService = TestBed.inject(SettingService);
      optionService = TestBed.inject(OptionService);
      optionValueService = TestBed.inject(OptionValueService);
      deviceService = TestBed.inject(DeviceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Option query and add missing value', () => {
        const setting: ISetting = { id: 456 };
        const option: IOption = { id: 24680 };
        setting.option = option;

        const optionCollection: IOption[] = [{ id: 4030 }];
        jest.spyOn(optionService, 'query').mockReturnValue(of(new HttpResponse({ body: optionCollection })));
        const additionalOptions = [option];
        const expectedCollection: IOption[] = [...additionalOptions, ...optionCollection];
        jest.spyOn(optionService, 'addOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ setting });
        comp.ngOnInit();

        expect(optionService.query).toHaveBeenCalled();
        expect(optionService.addOptionToCollectionIfMissing).toHaveBeenCalledWith(optionCollection, ...additionalOptions);
        expect(comp.optionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OptionValue query and add missing value', () => {
        const setting: ISetting = { id: 456 };
        const selectedValues: IOptionValue[] = [{ id: 70451 }];
        setting.selectedValues = selectedValues;

        const optionValueCollection: IOptionValue[] = [{ id: 49289 }];
        jest.spyOn(optionValueService, 'query').mockReturnValue(of(new HttpResponse({ body: optionValueCollection })));
        const additionalOptionValues = [...selectedValues];
        const expectedCollection: IOptionValue[] = [...additionalOptionValues, ...optionValueCollection];
        jest.spyOn(optionValueService, 'addOptionValueToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ setting });
        comp.ngOnInit();

        expect(optionValueService.query).toHaveBeenCalled();
        expect(optionValueService.addOptionValueToCollectionIfMissing).toHaveBeenCalledWith(
          optionValueCollection,
          ...additionalOptionValues
        );
        expect(comp.optionValuesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Device query and add missing value', () => {
        const setting: ISetting = { id: 456 };
        const device: IDevice = { id: 41124 };
        setting.device = device;

        const deviceCollection: IDevice[] = [{ id: 81977 }];
        jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
        const additionalDevices = [device];
        const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
        jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ setting });
        comp.ngOnInit();

        expect(deviceService.query).toHaveBeenCalled();
        expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(deviceCollection, ...additionalDevices);
        expect(comp.devicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const setting: ISetting = { id: 456 };
        const option: IOption = { id: 26566 };
        setting.option = option;
        const selectedValues: IOptionValue = { id: 51381 };
        setting.selectedValues = [selectedValues];
        const device: IDevice = { id: 11065 };
        setting.device = device;

        activatedRoute.data = of({ setting });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(setting));
        expect(comp.optionsSharedCollection).toContain(option);
        expect(comp.optionValuesSharedCollection).toContain(selectedValues);
        expect(comp.devicesSharedCollection).toContain(device);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Setting>>();
        const setting = { id: 123 };
        jest.spyOn(settingService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ setting });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: setting }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(settingService.update).toHaveBeenCalledWith(setting);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Setting>>();
        const setting = new Setting();
        jest.spyOn(settingService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ setting });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: setting }));
        saveSubject.complete();

        // THEN
        expect(settingService.create).toHaveBeenCalledWith(setting);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Setting>>();
        const setting = { id: 123 };
        jest.spyOn(settingService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ setting });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(settingService.update).toHaveBeenCalledWith(setting);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOptionById', () => {
        it('Should return tracked Option primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOptionById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackOptionValueById', () => {
        it('Should return tracked OptionValue primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOptionValueById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDeviceById', () => {
        it('Should return tracked Device primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDeviceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedOptionValue', () => {
        it('Should return option if no OptionValue is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedOptionValue(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected OptionValue for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedOptionValue(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this OptionValue is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedOptionValue(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DeviceService } from '../service/device.service';
import { IDevice, Device } from '../device.model';
import { IDeviceModel } from 'app/entities/device-model/device-model.model';
import { DeviceModelService } from 'app/entities/device-model/service/device-model.service';
import { IResponsiblePerson } from 'app/entities/responsible-person/responsible-person.model';
import { ResponsiblePersonService } from 'app/entities/responsible-person/service/responsible-person.service';

import { DeviceUpdateComponent } from './device-update.component';

describe('Component Tests', () => {
  describe('Device Management Update Component', () => {
    let comp: DeviceUpdateComponent;
    let fixture: ComponentFixture<DeviceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let deviceService: DeviceService;
    let deviceModelService: DeviceModelService;
    let responsiblePersonService: ResponsiblePersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DeviceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DeviceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeviceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      deviceService = TestBed.inject(DeviceService);
      deviceModelService = TestBed.inject(DeviceModelService);
      responsiblePersonService = TestBed.inject(ResponsiblePersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Device query and add missing value', () => {
        const device: IDevice = { id: 456 };
        const parent: IDevice = { id: 21615 };
        device.parent = parent;

        const deviceCollection: IDevice[] = [{ id: 53832 }];
        jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
        const additionalDevices = [parent];
        const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
        jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ device });
        comp.ngOnInit();

        expect(deviceService.query).toHaveBeenCalled();
        expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(deviceCollection, ...additionalDevices);
        expect(comp.devicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call DeviceModel query and add missing value', () => {
        const device: IDevice = { id: 456 };
        const model: IDeviceModel = { id: 84116 };
        device.model = model;

        const deviceModelCollection: IDeviceModel[] = [{ id: 81611 }];
        jest.spyOn(deviceModelService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceModelCollection })));
        const additionalDeviceModels = [model];
        const expectedCollection: IDeviceModel[] = [...additionalDeviceModels, ...deviceModelCollection];
        jest.spyOn(deviceModelService, 'addDeviceModelToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ device });
        comp.ngOnInit();

        expect(deviceModelService.query).toHaveBeenCalled();
        expect(deviceModelService.addDeviceModelToCollectionIfMissing).toHaveBeenCalledWith(
          deviceModelCollection,
          ...additionalDeviceModels
        );
        expect(comp.deviceModelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ResponsiblePerson query and add missing value', () => {
        const device: IDevice = { id: 456 };
        const responsiblePerson: IResponsiblePerson = { id: 47473 };
        device.responsiblePerson = responsiblePerson;

        const responsiblePersonCollection: IResponsiblePerson[] = [{ id: 42664 }];
        jest.spyOn(responsiblePersonService, 'query').mockReturnValue(of(new HttpResponse({ body: responsiblePersonCollection })));
        const additionalResponsiblePeople = [responsiblePerson];
        const expectedCollection: IResponsiblePerson[] = [...additionalResponsiblePeople, ...responsiblePersonCollection];
        jest.spyOn(responsiblePersonService, 'addResponsiblePersonToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ device });
        comp.ngOnInit();

        expect(responsiblePersonService.query).toHaveBeenCalled();
        expect(responsiblePersonService.addResponsiblePersonToCollectionIfMissing).toHaveBeenCalledWith(
          responsiblePersonCollection,
          ...additionalResponsiblePeople
        );
        expect(comp.responsiblePeopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const device: IDevice = { id: 456 };
        const parent: IDevice = { id: 92561 };
        device.parent = parent;
        const model: IDeviceModel = { id: 69603 };
        device.model = model;
        const responsiblePerson: IResponsiblePerson = { id: 17880 };
        device.responsiblePerson = responsiblePerson;

        activatedRoute.data = of({ device });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(device));
        expect(comp.devicesSharedCollection).toContain(parent);
        expect(comp.deviceModelsSharedCollection).toContain(model);
        expect(comp.responsiblePeopleSharedCollection).toContain(responsiblePerson);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Device>>();
        const device = { id: 123 };
        jest.spyOn(deviceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ device });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: device }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(deviceService.update).toHaveBeenCalledWith(device);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Device>>();
        const device = new Device();
        jest.spyOn(deviceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ device });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: device }));
        saveSubject.complete();

        // THEN
        expect(deviceService.create).toHaveBeenCalledWith(device);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Device>>();
        const device = { id: 123 };
        jest.spyOn(deviceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ device });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(deviceService.update).toHaveBeenCalledWith(device);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackDeviceById', () => {
        it('Should return tracked Device primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDeviceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDeviceModelById', () => {
        it('Should return tracked DeviceModel primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDeviceModelById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackResponsiblePersonById', () => {
        it('Should return tracked ResponsiblePerson primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackResponsiblePersonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

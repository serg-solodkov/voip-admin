jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VoipAccountService } from '../service/voip-account.service';
import { IVoipAccount, VoipAccount } from '../voip-account.model';
import { IAsteriskAccount } from 'app/entities/asterisk-account/asterisk-account.model';
import { AsteriskAccountService } from 'app/entities/asterisk-account/service/asterisk-account.service';
import { IDevice } from 'app/entities/device/device.model';
import { DeviceService } from 'app/entities/device/service/device.service';

import { VoipAccountUpdateComponent } from './voip-account-update.component';

describe('Component Tests', () => {
  describe('VoipAccount Management Update Component', () => {
    let comp: VoipAccountUpdateComponent;
    let fixture: ComponentFixture<VoipAccountUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let voipAccountService: VoipAccountService;
    let asteriskAccountService: AsteriskAccountService;
    let deviceService: DeviceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VoipAccountUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VoipAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VoipAccountUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      voipAccountService = TestBed.inject(VoipAccountService);
      asteriskAccountService = TestBed.inject(AsteriskAccountService);
      deviceService = TestBed.inject(DeviceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call asteriskAccount query and add missing value', () => {
        const voipAccount: IVoipAccount = { id: 456 };
        const asteriskAccount: IAsteriskAccount = { id: 92721 };
        voipAccount.asteriskAccount = asteriskAccount;

        const asteriskAccountCollection: IAsteriskAccount[] = [{ id: 45370 }];
        jest.spyOn(asteriskAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: asteriskAccountCollection })));
        const expectedCollection: IAsteriskAccount[] = [asteriskAccount, ...asteriskAccountCollection];
        jest.spyOn(asteriskAccountService, 'addAsteriskAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ voipAccount });
        comp.ngOnInit();

        expect(asteriskAccountService.query).toHaveBeenCalled();
        expect(asteriskAccountService.addAsteriskAccountToCollectionIfMissing).toHaveBeenCalledWith(
          asteriskAccountCollection,
          asteriskAccount
        );
        expect(comp.asteriskAccountsCollection).toEqual(expectedCollection);
      });

      it('Should call Device query and add missing value', () => {
        const voipAccount: IVoipAccount = { id: 456 };
        const device: IDevice = { id: 92275 };
        voipAccount.device = device;

        const deviceCollection: IDevice[] = [{ id: 84216 }];
        jest.spyOn(deviceService, 'query').mockReturnValue(of(new HttpResponse({ body: deviceCollection })));
        const additionalDevices = [device];
        const expectedCollection: IDevice[] = [...additionalDevices, ...deviceCollection];
        jest.spyOn(deviceService, 'addDeviceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ voipAccount });
        comp.ngOnInit();

        expect(deviceService.query).toHaveBeenCalled();
        expect(deviceService.addDeviceToCollectionIfMissing).toHaveBeenCalledWith(deviceCollection, ...additionalDevices);
        expect(comp.devicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const voipAccount: IVoipAccount = { id: 456 };
        const asteriskAccount: IAsteriskAccount = { id: 72270 };
        voipAccount.asteriskAccount = asteriskAccount;
        const device: IDevice = { id: 57923 };
        voipAccount.device = device;

        activatedRoute.data = of({ voipAccount });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(voipAccount));
        expect(comp.asteriskAccountsCollection).toContain(asteriskAccount);
        expect(comp.devicesSharedCollection).toContain(device);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<VoipAccount>>();
        const voipAccount = { id: 123 };
        jest.spyOn(voipAccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ voipAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: voipAccount }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(voipAccountService.update).toHaveBeenCalledWith(voipAccount);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<VoipAccount>>();
        const voipAccount = new VoipAccount();
        jest.spyOn(voipAccountService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ voipAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: voipAccount }));
        saveSubject.complete();

        // THEN
        expect(voipAccountService.create).toHaveBeenCalledWith(voipAccount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<VoipAccount>>();
        const voipAccount = { id: 123 };
        jest.spyOn(voipAccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ voipAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(voipAccountService.update).toHaveBeenCalledWith(voipAccount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAsteriskAccountById', () => {
        it('Should return tracked AsteriskAccount primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAsteriskAccountById(0, entity);
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
  });
});

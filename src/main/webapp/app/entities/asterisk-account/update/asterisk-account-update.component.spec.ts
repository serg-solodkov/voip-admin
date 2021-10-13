jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AsteriskAccountService } from '../service/asterisk-account.service';
import { IAsteriskAccount, AsteriskAccount } from '../asterisk-account.model';

import { AsteriskAccountUpdateComponent } from './asterisk-account-update.component';

describe('Component Tests', () => {
  describe('AsteriskAccount Management Update Component', () => {
    let comp: AsteriskAccountUpdateComponent;
    let fixture: ComponentFixture<AsteriskAccountUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let asteriskAccountService: AsteriskAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AsteriskAccountUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AsteriskAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AsteriskAccountUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      asteriskAccountService = TestBed.inject(AsteriskAccountService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const asteriskAccount: IAsteriskAccount = { id: 456 };

        activatedRoute.data = of({ asteriskAccount });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(asteriskAccount));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AsteriskAccount>>();
        const asteriskAccount = { id: 123 };
        jest.spyOn(asteriskAccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ asteriskAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: asteriskAccount }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(asteriskAccountService.update).toHaveBeenCalledWith(asteriskAccount);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AsteriskAccount>>();
        const asteriskAccount = new AsteriskAccount();
        jest.spyOn(asteriskAccountService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ asteriskAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: asteriskAccount }));
        saveSubject.complete();

        // THEN
        expect(asteriskAccountService.create).toHaveBeenCalledWith(asteriskAccount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AsteriskAccount>>();
        const asteriskAccount = { id: 123 };
        jest.spyOn(asteriskAccountService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ asteriskAccount });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(asteriskAccountService.update).toHaveBeenCalledWith(asteriskAccount);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OptionValueService } from '../service/option-value.service';
import { IOptionValue, OptionValue } from '../option-value.model';
import { IOption } from 'app/entities/option/option.model';
import { OptionService } from 'app/entities/option/service/option.service';

import { OptionValueUpdateComponent } from './option-value-update.component';

describe('Component Tests', () => {
  describe('OptionValue Management Update Component', () => {
    let comp: OptionValueUpdateComponent;
    let fixture: ComponentFixture<OptionValueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let optionValueService: OptionValueService;
    let optionService: OptionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OptionValueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OptionValueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OptionValueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      optionValueService = TestBed.inject(OptionValueService);
      optionService = TestBed.inject(OptionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Option query and add missing value', () => {
        const optionValue: IOptionValue = { id: 456 };
        const option: IOption = { id: 39427 };
        optionValue.option = option;

        const optionCollection: IOption[] = [{ id: 28875 }];
        jest.spyOn(optionService, 'query').mockReturnValue(of(new HttpResponse({ body: optionCollection })));
        const additionalOptions = [option];
        const expectedCollection: IOption[] = [...additionalOptions, ...optionCollection];
        jest.spyOn(optionService, 'addOptionToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ optionValue });
        comp.ngOnInit();

        expect(optionService.query).toHaveBeenCalled();
        expect(optionService.addOptionToCollectionIfMissing).toHaveBeenCalledWith(optionCollection, ...additionalOptions);
        expect(comp.optionsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const optionValue: IOptionValue = { id: 456 };
        const option: IOption = { id: 23694 };
        optionValue.option = option;

        activatedRoute.data = of({ optionValue });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(optionValue));
        expect(comp.optionsSharedCollection).toContain(option);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OptionValue>>();
        const optionValue = { id: 123 };
        jest.spyOn(optionValueService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ optionValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: optionValue }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(optionValueService.update).toHaveBeenCalledWith(optionValue);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OptionValue>>();
        const optionValue = new OptionValue();
        jest.spyOn(optionValueService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ optionValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: optionValue }));
        saveSubject.complete();

        // THEN
        expect(optionValueService.create).toHaveBeenCalledWith(optionValue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OptionValue>>();
        const optionValue = { id: 123 };
        jest.spyOn(optionValueService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ optionValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(optionValueService.update).toHaveBeenCalledWith(optionValue);
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
    });
  });
});

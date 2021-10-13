import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOptionValue, OptionValue } from '../option-value.model';

import { OptionValueService } from './option-value.service';

describe('Service Tests', () => {
  describe('OptionValue Service', () => {
    let service: OptionValueService;
    let httpMock: HttpTestingController;
    let elemDefault: IOptionValue;
    let expectedResult: IOptionValue | IOptionValue[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OptionValueService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        value: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a OptionValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OptionValue()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OptionValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            value: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OptionValue', () => {
        const patchObject = Object.assign(
          {
            value: 'BBBBBB',
          },
          new OptionValue()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OptionValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            value: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a OptionValue', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOptionValueToCollectionIfMissing', () => {
        it('should add a OptionValue to an empty array', () => {
          const optionValue: IOptionValue = { id: 123 };
          expectedResult = service.addOptionValueToCollectionIfMissing([], optionValue);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(optionValue);
        });

        it('should not add a OptionValue to an array that contains it', () => {
          const optionValue: IOptionValue = { id: 123 };
          const optionValueCollection: IOptionValue[] = [
            {
              ...optionValue,
            },
            { id: 456 },
          ];
          expectedResult = service.addOptionValueToCollectionIfMissing(optionValueCollection, optionValue);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OptionValue to an array that doesn't contain it", () => {
          const optionValue: IOptionValue = { id: 123 };
          const optionValueCollection: IOptionValue[] = [{ id: 456 }];
          expectedResult = service.addOptionValueToCollectionIfMissing(optionValueCollection, optionValue);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(optionValue);
        });

        it('should add only unique OptionValue to an array', () => {
          const optionValueArray: IOptionValue[] = [{ id: 123 }, { id: 456 }, { id: 43080 }];
          const optionValueCollection: IOptionValue[] = [{ id: 123 }];
          expectedResult = service.addOptionValueToCollectionIfMissing(optionValueCollection, ...optionValueArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const optionValue: IOptionValue = { id: 123 };
          const optionValue2: IOptionValue = { id: 456 };
          expectedResult = service.addOptionValueToCollectionIfMissing([], optionValue, optionValue2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(optionValue);
          expect(expectedResult).toContain(optionValue2);
        });

        it('should accept null and undefined values', () => {
          const optionValue: IOptionValue = { id: 123 };
          expectedResult = service.addOptionValueToCollectionIfMissing([], null, optionValue, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(optionValue);
        });

        it('should return initial array if no OptionValue is added', () => {
          const optionValueCollection: IOptionValue[] = [{ id: 123 }];
          expectedResult = service.addOptionValueToCollectionIfMissing(optionValueCollection, undefined, null);
          expect(expectedResult).toEqual(optionValueCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

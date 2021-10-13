import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { OptionValueType } from 'app/entities/enumerations/option-value-type.model';
import { IOption, Option } from '../option.model';

import { OptionService } from './option.service';

describe('Service Tests', () => {
  describe('Option Service', () => {
    let service: OptionService;
    let httpMock: HttpTestingController;
    let elemDefault: IOption;
    let expectedResult: IOption | IOption[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OptionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        code: 'AAAAAAA',
        descr: 'AAAAAAA',
        valueType: OptionValueType.TEXT,
        multiple: false,
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

      it('should create a Option', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Option()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Option', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            descr: 'BBBBBB',
            valueType: 'BBBBBB',
            multiple: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Option', () => {
        const patchObject = Object.assign(
          {
            descr: 'BBBBBB',
          },
          new Option()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Option', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            descr: 'BBBBBB',
            valueType: 'BBBBBB',
            multiple: true,
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

      it('should delete a Option', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOptionToCollectionIfMissing', () => {
        it('should add a Option to an empty array', () => {
          const option: IOption = { id: 123 };
          expectedResult = service.addOptionToCollectionIfMissing([], option);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(option);
        });

        it('should not add a Option to an array that contains it', () => {
          const option: IOption = { id: 123 };
          const optionCollection: IOption[] = [
            {
              ...option,
            },
            { id: 456 },
          ];
          expectedResult = service.addOptionToCollectionIfMissing(optionCollection, option);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Option to an array that doesn't contain it", () => {
          const option: IOption = { id: 123 };
          const optionCollection: IOption[] = [{ id: 456 }];
          expectedResult = service.addOptionToCollectionIfMissing(optionCollection, option);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(option);
        });

        it('should add only unique Option to an array', () => {
          const optionArray: IOption[] = [{ id: 123 }, { id: 456 }, { id: 79389 }];
          const optionCollection: IOption[] = [{ id: 123 }];
          expectedResult = service.addOptionToCollectionIfMissing(optionCollection, ...optionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const option: IOption = { id: 123 };
          const option2: IOption = { id: 456 };
          expectedResult = service.addOptionToCollectionIfMissing([], option, option2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(option);
          expect(expectedResult).toContain(option2);
        });

        it('should accept null and undefined values', () => {
          const option: IOption = { id: 123 };
          expectedResult = service.addOptionToCollectionIfMissing([], null, option, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(option);
        });

        it('should return initial array if no Option is added', () => {
          const optionCollection: IOption[] = [{ id: 123 }];
          expectedResult = service.addOptionToCollectionIfMissing(optionCollection, undefined, null);
          expect(expectedResult).toEqual(optionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

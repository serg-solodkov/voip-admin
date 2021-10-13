import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOtherDeviceType, OtherDeviceType } from '../other-device-type.model';

import { OtherDeviceTypeService } from './other-device-type.service';

describe('Service Tests', () => {
  describe('OtherDeviceType Service', () => {
    let service: OtherDeviceTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IOtherDeviceType;
    let expectedResult: IOtherDeviceType | IOtherDeviceType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OtherDeviceTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
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

      it('should create a OtherDeviceType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new OtherDeviceType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a OtherDeviceType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a OtherDeviceType', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          new OtherDeviceType()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of OtherDeviceType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
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

      it('should delete a OtherDeviceType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOtherDeviceTypeToCollectionIfMissing', () => {
        it('should add a OtherDeviceType to an empty array', () => {
          const otherDeviceType: IOtherDeviceType = { id: 123 };
          expectedResult = service.addOtherDeviceTypeToCollectionIfMissing([], otherDeviceType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(otherDeviceType);
        });

        it('should not add a OtherDeviceType to an array that contains it', () => {
          const otherDeviceType: IOtherDeviceType = { id: 123 };
          const otherDeviceTypeCollection: IOtherDeviceType[] = [
            {
              ...otherDeviceType,
            },
            { id: 456 },
          ];
          expectedResult = service.addOtherDeviceTypeToCollectionIfMissing(otherDeviceTypeCollection, otherDeviceType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a OtherDeviceType to an array that doesn't contain it", () => {
          const otherDeviceType: IOtherDeviceType = { id: 123 };
          const otherDeviceTypeCollection: IOtherDeviceType[] = [{ id: 456 }];
          expectedResult = service.addOtherDeviceTypeToCollectionIfMissing(otherDeviceTypeCollection, otherDeviceType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(otherDeviceType);
        });

        it('should add only unique OtherDeviceType to an array', () => {
          const otherDeviceTypeArray: IOtherDeviceType[] = [{ id: 123 }, { id: 456 }, { id: 22688 }];
          const otherDeviceTypeCollection: IOtherDeviceType[] = [{ id: 123 }];
          expectedResult = service.addOtherDeviceTypeToCollectionIfMissing(otherDeviceTypeCollection, ...otherDeviceTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const otherDeviceType: IOtherDeviceType = { id: 123 };
          const otherDeviceType2: IOtherDeviceType = { id: 456 };
          expectedResult = service.addOtherDeviceTypeToCollectionIfMissing([], otherDeviceType, otherDeviceType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(otherDeviceType);
          expect(expectedResult).toContain(otherDeviceType2);
        });

        it('should accept null and undefined values', () => {
          const otherDeviceType: IOtherDeviceType = { id: 123 };
          expectedResult = service.addOtherDeviceTypeToCollectionIfMissing([], null, otherDeviceType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(otherDeviceType);
        });

        it('should return initial array if no OtherDeviceType is added', () => {
          const otherDeviceTypeCollection: IOtherDeviceType[] = [{ id: 123 }];
          expectedResult = service.addOtherDeviceTypeToCollectionIfMissing(otherDeviceTypeCollection, undefined, null);
          expect(expectedResult).toEqual(otherDeviceTypeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

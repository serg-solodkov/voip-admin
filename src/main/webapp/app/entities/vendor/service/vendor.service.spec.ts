import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVendor, Vendor } from '../vendor.model';

import { VendorService } from './vendor.service';

describe('Service Tests', () => {
  describe('Vendor Service', () => {
    let service: VendorService;
    let httpMock: HttpTestingController;
    let elemDefault: IVendor;
    let expectedResult: IVendor | IVendor[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VendorService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
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

      it('should create a Vendor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Vendor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vendor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Vendor', () => {
        const patchObject = Object.assign({}, new Vendor());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Vendor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
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

      it('should delete a Vendor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVendorToCollectionIfMissing', () => {
        it('should add a Vendor to an empty array', () => {
          const vendor: IVendor = { id: 123 };
          expectedResult = service.addVendorToCollectionIfMissing([], vendor);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vendor);
        });

        it('should not add a Vendor to an array that contains it', () => {
          const vendor: IVendor = { id: 123 };
          const vendorCollection: IVendor[] = [
            {
              ...vendor,
            },
            { id: 456 },
          ];
          expectedResult = service.addVendorToCollectionIfMissing(vendorCollection, vendor);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Vendor to an array that doesn't contain it", () => {
          const vendor: IVendor = { id: 123 };
          const vendorCollection: IVendor[] = [{ id: 456 }];
          expectedResult = service.addVendorToCollectionIfMissing(vendorCollection, vendor);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vendor);
        });

        it('should add only unique Vendor to an array', () => {
          const vendorArray: IVendor[] = [{ id: 123 }, { id: 456 }, { id: 48041 }];
          const vendorCollection: IVendor[] = [{ id: 123 }];
          expectedResult = service.addVendorToCollectionIfMissing(vendorCollection, ...vendorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const vendor: IVendor = { id: 123 };
          const vendor2: IVendor = { id: 456 };
          expectedResult = service.addVendorToCollectionIfMissing([], vendor, vendor2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vendor);
          expect(expectedResult).toContain(vendor2);
        });

        it('should accept null and undefined values', () => {
          const vendor: IVendor = { id: 123 };
          expectedResult = service.addVendorToCollectionIfMissing([], null, vendor, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vendor);
        });

        it('should return initial array if no Vendor is added', () => {
          const vendorCollection: IVendor[] = [{ id: 123 }];
          expectedResult = service.addVendorToCollectionIfMissing(vendorCollection, undefined, null);
          expect(expectedResult).toEqual(vendorCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

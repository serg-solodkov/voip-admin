import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAsteriskAccount, AsteriskAccount } from '../asterisk-account.model';

import { AsteriskAccountService } from './asterisk-account.service';

describe('Service Tests', () => {
  describe('AsteriskAccount Service', () => {
    let service: AsteriskAccountService;
    let httpMock: HttpTestingController;
    let elemDefault: IAsteriskAccount;
    let expectedResult: IAsteriskAccount | IAsteriskAccount[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AsteriskAccountService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        username: 'AAAAAAA',
        asteriskId: 'AAAAAAA',
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

      it('should create a AsteriskAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AsteriskAccount()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AsteriskAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            username: 'BBBBBB',
            asteriskId: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AsteriskAccount', () => {
        const patchObject = Object.assign(
          {
            username: 'BBBBBB',
            asteriskId: 'BBBBBB',
          },
          new AsteriskAccount()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AsteriskAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            username: 'BBBBBB',
            asteriskId: 'BBBBBB',
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

      it('should delete a AsteriskAccount', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAsteriskAccountToCollectionIfMissing', () => {
        it('should add a AsteriskAccount to an empty array', () => {
          const asteriskAccount: IAsteriskAccount = { id: 123 };
          expectedResult = service.addAsteriskAccountToCollectionIfMissing([], asteriskAccount);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(asteriskAccount);
        });

        it('should not add a AsteriskAccount to an array that contains it', () => {
          const asteriskAccount: IAsteriskAccount = { id: 123 };
          const asteriskAccountCollection: IAsteriskAccount[] = [
            {
              ...asteriskAccount,
            },
            { id: 456 },
          ];
          expectedResult = service.addAsteriskAccountToCollectionIfMissing(asteriskAccountCollection, asteriskAccount);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AsteriskAccount to an array that doesn't contain it", () => {
          const asteriskAccount: IAsteriskAccount = { id: 123 };
          const asteriskAccountCollection: IAsteriskAccount[] = [{ id: 456 }];
          expectedResult = service.addAsteriskAccountToCollectionIfMissing(asteriskAccountCollection, asteriskAccount);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(asteriskAccount);
        });

        it('should add only unique AsteriskAccount to an array', () => {
          const asteriskAccountArray: IAsteriskAccount[] = [{ id: 123 }, { id: 456 }, { id: 85977 }];
          const asteriskAccountCollection: IAsteriskAccount[] = [{ id: 123 }];
          expectedResult = service.addAsteriskAccountToCollectionIfMissing(asteriskAccountCollection, ...asteriskAccountArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const asteriskAccount: IAsteriskAccount = { id: 123 };
          const asteriskAccount2: IAsteriskAccount = { id: 456 };
          expectedResult = service.addAsteriskAccountToCollectionIfMissing([], asteriskAccount, asteriskAccount2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(asteriskAccount);
          expect(expectedResult).toContain(asteriskAccount2);
        });

        it('should accept null and undefined values', () => {
          const asteriskAccount: IAsteriskAccount = { id: 123 };
          expectedResult = service.addAsteriskAccountToCollectionIfMissing([], null, asteriskAccount, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(asteriskAccount);
        });

        it('should return initial array if no AsteriskAccount is added', () => {
          const asteriskAccountCollection: IAsteriskAccount[] = [{ id: 123 }];
          expectedResult = service.addAsteriskAccountToCollectionIfMissing(asteriskAccountCollection, undefined, null);
          expect(expectedResult).toEqual(asteriskAccountCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

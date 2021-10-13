import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVoipAccount, VoipAccount } from '../voip-account.model';

import { VoipAccountService } from './voip-account.service';

describe('Service Tests', () => {
  describe('VoipAccount Service', () => {
    let service: VoipAccountService;
    let httpMock: HttpTestingController;
    let elemDefault: IVoipAccount;
    let expectedResult: IVoipAccount | IVoipAccount[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VoipAccountService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        manuallyCreated: false,
        username: 'AAAAAAA',
        password: 'AAAAAAA',
        sipServer: 'AAAAAAA',
        sipPort: 'AAAAAAA',
        lineEnable: false,
        lineNumber: 'AAAAAAA',
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

      it('should create a VoipAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new VoipAccount()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a VoipAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            manuallyCreated: true,
            username: 'BBBBBB',
            password: 'BBBBBB',
            sipServer: 'BBBBBB',
            sipPort: 'BBBBBB',
            lineEnable: true,
            lineNumber: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a VoipAccount', () => {
        const patchObject = Object.assign(
          {
            username: 'BBBBBB',
            sipServer: 'BBBBBB',
            sipPort: 'BBBBBB',
            lineEnable: true,
          },
          new VoipAccount()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of VoipAccount', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            manuallyCreated: true,
            username: 'BBBBBB',
            password: 'BBBBBB',
            sipServer: 'BBBBBB',
            sipPort: 'BBBBBB',
            lineEnable: true,
            lineNumber: 'BBBBBB',
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

      it('should delete a VoipAccount', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVoipAccountToCollectionIfMissing', () => {
        it('should add a VoipAccount to an empty array', () => {
          const voipAccount: IVoipAccount = { id: 123 };
          expectedResult = service.addVoipAccountToCollectionIfMissing([], voipAccount);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(voipAccount);
        });

        it('should not add a VoipAccount to an array that contains it', () => {
          const voipAccount: IVoipAccount = { id: 123 };
          const voipAccountCollection: IVoipAccount[] = [
            {
              ...voipAccount,
            },
            { id: 456 },
          ];
          expectedResult = service.addVoipAccountToCollectionIfMissing(voipAccountCollection, voipAccount);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a VoipAccount to an array that doesn't contain it", () => {
          const voipAccount: IVoipAccount = { id: 123 };
          const voipAccountCollection: IVoipAccount[] = [{ id: 456 }];
          expectedResult = service.addVoipAccountToCollectionIfMissing(voipAccountCollection, voipAccount);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(voipAccount);
        });

        it('should add only unique VoipAccount to an array', () => {
          const voipAccountArray: IVoipAccount[] = [{ id: 123 }, { id: 456 }, { id: 33189 }];
          const voipAccountCollection: IVoipAccount[] = [{ id: 123 }];
          expectedResult = service.addVoipAccountToCollectionIfMissing(voipAccountCollection, ...voipAccountArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const voipAccount: IVoipAccount = { id: 123 };
          const voipAccount2: IVoipAccount = { id: 456 };
          expectedResult = service.addVoipAccountToCollectionIfMissing([], voipAccount, voipAccount2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(voipAccount);
          expect(expectedResult).toContain(voipAccount2);
        });

        it('should accept null and undefined values', () => {
          const voipAccount: IVoipAccount = { id: 123 };
          expectedResult = service.addVoipAccountToCollectionIfMissing([], null, voipAccount, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(voipAccount);
        });

        it('should return initial array if no VoipAccount is added', () => {
          const voipAccountCollection: IVoipAccount[] = [{ id: 123 }];
          expectedResult = service.addVoipAccountToCollectionIfMissing(voipAccountCollection, undefined, null);
          expect(expectedResult).toEqual(voipAccountCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

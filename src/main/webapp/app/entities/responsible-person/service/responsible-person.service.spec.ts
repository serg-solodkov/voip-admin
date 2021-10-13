import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IResponsiblePerson, ResponsiblePerson } from '../responsible-person.model';

import { ResponsiblePersonService } from './responsible-person.service';

describe('Service Tests', () => {
  describe('ResponsiblePerson Service', () => {
    let service: ResponsiblePersonService;
    let httpMock: HttpTestingController;
    let elemDefault: IResponsiblePerson;
    let expectedResult: IResponsiblePerson | IResponsiblePerson[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ResponsiblePersonService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        code: 'AAAAAAA',
        firstName: 'AAAAAAA',
        secondName: 'AAAAAAA',
        lastName: 'AAAAAAA',
        position: 'AAAAAAA',
        room: 'AAAAAAA',
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

      it('should create a ResponsiblePerson', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ResponsiblePerson()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ResponsiblePerson', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            firstName: 'BBBBBB',
            secondName: 'BBBBBB',
            lastName: 'BBBBBB',
            position: 'BBBBBB',
            room: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ResponsiblePerson', () => {
        const patchObject = Object.assign({}, new ResponsiblePerson());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ResponsiblePerson', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            firstName: 'BBBBBB',
            secondName: 'BBBBBB',
            lastName: 'BBBBBB',
            position: 'BBBBBB',
            room: 'BBBBBB',
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

      it('should delete a ResponsiblePerson', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addResponsiblePersonToCollectionIfMissing', () => {
        it('should add a ResponsiblePerson to an empty array', () => {
          const responsiblePerson: IResponsiblePerson = { id: 123 };
          expectedResult = service.addResponsiblePersonToCollectionIfMissing([], responsiblePerson);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(responsiblePerson);
        });

        it('should not add a ResponsiblePerson to an array that contains it', () => {
          const responsiblePerson: IResponsiblePerson = { id: 123 };
          const responsiblePersonCollection: IResponsiblePerson[] = [
            {
              ...responsiblePerson,
            },
            { id: 456 },
          ];
          expectedResult = service.addResponsiblePersonToCollectionIfMissing(responsiblePersonCollection, responsiblePerson);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ResponsiblePerson to an array that doesn't contain it", () => {
          const responsiblePerson: IResponsiblePerson = { id: 123 };
          const responsiblePersonCollection: IResponsiblePerson[] = [{ id: 456 }];
          expectedResult = service.addResponsiblePersonToCollectionIfMissing(responsiblePersonCollection, responsiblePerson);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(responsiblePerson);
        });

        it('should add only unique ResponsiblePerson to an array', () => {
          const responsiblePersonArray: IResponsiblePerson[] = [{ id: 123 }, { id: 456 }, { id: 49237 }];
          const responsiblePersonCollection: IResponsiblePerson[] = [{ id: 123 }];
          expectedResult = service.addResponsiblePersonToCollectionIfMissing(responsiblePersonCollection, ...responsiblePersonArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const responsiblePerson: IResponsiblePerson = { id: 123 };
          const responsiblePerson2: IResponsiblePerson = { id: 456 };
          expectedResult = service.addResponsiblePersonToCollectionIfMissing([], responsiblePerson, responsiblePerson2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(responsiblePerson);
          expect(expectedResult).toContain(responsiblePerson2);
        });

        it('should accept null and undefined values', () => {
          const responsiblePerson: IResponsiblePerson = { id: 123 };
          expectedResult = service.addResponsiblePersonToCollectionIfMissing([], null, responsiblePerson, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(responsiblePerson);
        });

        it('should return initial array if no ResponsiblePerson is added', () => {
          const responsiblePersonCollection: IResponsiblePerson[] = [{ id: 123 }];
          expectedResult = service.addResponsiblePersonToCollectionIfMissing(responsiblePersonCollection, undefined, null);
          expect(expectedResult).toEqual(responsiblePersonCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

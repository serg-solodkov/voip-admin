jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IResponsiblePerson, ResponsiblePerson } from '../responsible-person.model';
import { ResponsiblePersonService } from '../service/responsible-person.service';

import { ResponsiblePersonRoutingResolveService } from './responsible-person-routing-resolve.service';

describe('Service Tests', () => {
  describe('ResponsiblePerson routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ResponsiblePersonRoutingResolveService;
    let service: ResponsiblePersonService;
    let resultResponsiblePerson: IResponsiblePerson | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ResponsiblePersonRoutingResolveService);
      service = TestBed.inject(ResponsiblePersonService);
      resultResponsiblePerson = undefined;
    });

    describe('resolve', () => {
      it('should return IResponsiblePerson returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResponsiblePerson = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultResponsiblePerson).toEqual({ id: 123 });
      });

      it('should return new IResponsiblePerson if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResponsiblePerson = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultResponsiblePerson).toEqual(new ResponsiblePerson());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ResponsiblePerson })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResponsiblePerson = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultResponsiblePerson).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

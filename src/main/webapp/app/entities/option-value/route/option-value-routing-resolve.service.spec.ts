jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOptionValue, OptionValue } from '../option-value.model';
import { OptionValueService } from '../service/option-value.service';

import { OptionValueRoutingResolveService } from './option-value-routing-resolve.service';

describe('Service Tests', () => {
  describe('OptionValue routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OptionValueRoutingResolveService;
    let service: OptionValueService;
    let resultOptionValue: IOptionValue | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OptionValueRoutingResolveService);
      service = TestBed.inject(OptionValueService);
      resultOptionValue = undefined;
    });

    describe('resolve', () => {
      it('should return IOptionValue returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOptionValue = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOptionValue).toEqual({ id: 123 });
      });

      it('should return new IOptionValue if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOptionValue = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOptionValue).toEqual(new OptionValue());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OptionValue })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOptionValue = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOptionValue).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

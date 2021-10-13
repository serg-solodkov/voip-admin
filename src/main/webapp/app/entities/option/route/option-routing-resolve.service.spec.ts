jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOption, Option } from '../option.model';
import { OptionService } from '../service/option.service';

import { OptionRoutingResolveService } from './option-routing-resolve.service';

describe('Service Tests', () => {
  describe('Option routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OptionRoutingResolveService;
    let service: OptionService;
    let resultOption: IOption | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OptionRoutingResolveService);
      service = TestBed.inject(OptionService);
      resultOption = undefined;
    });

    describe('resolve', () => {
      it('should return IOption returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOption = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOption).toEqual({ id: 123 });
      });

      it('should return new IOption if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOption = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOption).toEqual(new Option());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Option })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOption = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOption).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

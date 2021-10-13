jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOtherDeviceType, OtherDeviceType } from '../other-device-type.model';
import { OtherDeviceTypeService } from '../service/other-device-type.service';

import { OtherDeviceTypeRoutingResolveService } from './other-device-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('OtherDeviceType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OtherDeviceTypeRoutingResolveService;
    let service: OtherDeviceTypeService;
    let resultOtherDeviceType: IOtherDeviceType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OtherDeviceTypeRoutingResolveService);
      service = TestBed.inject(OtherDeviceTypeService);
      resultOtherDeviceType = undefined;
    });

    describe('resolve', () => {
      it('should return IOtherDeviceType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOtherDeviceType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOtherDeviceType).toEqual({ id: 123 });
      });

      it('should return new IOtherDeviceType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOtherDeviceType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOtherDeviceType).toEqual(new OtherDeviceType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OtherDeviceType })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOtherDeviceType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOtherDeviceType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

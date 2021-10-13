jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDeviceModel, DeviceModel } from '../device-model.model';
import { DeviceModelService } from '../service/device-model.service';

import { DeviceModelRoutingResolveService } from './device-model-routing-resolve.service';

describe('Service Tests', () => {
  describe('DeviceModel routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DeviceModelRoutingResolveService;
    let service: DeviceModelService;
    let resultDeviceModel: IDeviceModel | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DeviceModelRoutingResolveService);
      service = TestBed.inject(DeviceModelService);
      resultDeviceModel = undefined;
    });

    describe('resolve', () => {
      it('should return IDeviceModel returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeviceModel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDeviceModel).toEqual({ id: 123 });
      });

      it('should return new IDeviceModel if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeviceModel = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDeviceModel).toEqual(new DeviceModel());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DeviceModel })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDeviceModel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDeviceModel).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

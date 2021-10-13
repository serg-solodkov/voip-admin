jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISetting, Setting } from '../setting.model';
import { SettingService } from '../service/setting.service';

import { SettingRoutingResolveService } from './setting-routing-resolve.service';

describe('Service Tests', () => {
  describe('Setting routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SettingRoutingResolveService;
    let service: SettingService;
    let resultSetting: ISetting | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SettingRoutingResolveService);
      service = TestBed.inject(SettingService);
      resultSetting = undefined;
    });

    describe('resolve', () => {
      it('should return ISetting returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSetting = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSetting).toEqual({ id: 123 });
      });

      it('should return new ISetting if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSetting = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSetting).toEqual(new Setting());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Setting })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSetting = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSetting).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

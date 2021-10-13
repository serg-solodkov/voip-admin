jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAsteriskAccount, AsteriskAccount } from '../asterisk-account.model';
import { AsteriskAccountService } from '../service/asterisk-account.service';

import { AsteriskAccountRoutingResolveService } from './asterisk-account-routing-resolve.service';

describe('Service Tests', () => {
  describe('AsteriskAccount routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AsteriskAccountRoutingResolveService;
    let service: AsteriskAccountService;
    let resultAsteriskAccount: IAsteriskAccount | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AsteriskAccountRoutingResolveService);
      service = TestBed.inject(AsteriskAccountService);
      resultAsteriskAccount = undefined;
    });

    describe('resolve', () => {
      it('should return IAsteriskAccount returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAsteriskAccount = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAsteriskAccount).toEqual({ id: 123 });
      });

      it('should return new IAsteriskAccount if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAsteriskAccount = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAsteriskAccount).toEqual(new AsteriskAccount());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AsteriskAccount })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAsteriskAccount = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAsteriskAccount).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

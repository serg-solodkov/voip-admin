jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVoipAccount, VoipAccount } from '../voip-account.model';
import { VoipAccountService } from '../service/voip-account.service';

import { VoipAccountRoutingResolveService } from './voip-account-routing-resolve.service';

describe('Service Tests', () => {
  describe('VoipAccount routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VoipAccountRoutingResolveService;
    let service: VoipAccountService;
    let resultVoipAccount: IVoipAccount | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VoipAccountRoutingResolveService);
      service = TestBed.inject(VoipAccountService);
      resultVoipAccount = undefined;
    });

    describe('resolve', () => {
      it('should return IVoipAccount returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVoipAccount = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVoipAccount).toEqual({ id: 123 });
      });

      it('should return new IVoipAccount if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVoipAccount = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVoipAccount).toEqual(new VoipAccount());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as VoipAccount })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVoipAccount = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVoipAccount).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

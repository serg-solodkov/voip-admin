import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVendor, Vendor } from '../vendor.model';
import { VendorService } from '../service/vendor.service';

@Injectable({ providedIn: 'root' })
export class VendorRoutingResolveService implements Resolve<IVendor> {
  constructor(protected service: VendorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVendor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vendor: HttpResponse<Vendor>) => {
          if (vendor.body) {
            return of(vendor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Vendor());
  }
}

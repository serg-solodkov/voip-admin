import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOtherDeviceType, OtherDeviceType } from '../other-device-type.model';
import { OtherDeviceTypeService } from '../service/other-device-type.service';

@Injectable({ providedIn: 'root' })
export class OtherDeviceTypeRoutingResolveService implements Resolve<IOtherDeviceType> {
  constructor(protected service: OtherDeviceTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOtherDeviceType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((otherDeviceType: HttpResponse<OtherDeviceType>) => {
          if (otherDeviceType.body) {
            return of(otherDeviceType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OtherDeviceType());
  }
}

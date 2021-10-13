import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeviceModel, DeviceModel } from '../device-model.model';
import { DeviceModelService } from '../service/device-model.service';

@Injectable({ providedIn: 'root' })
export class DeviceModelRoutingResolveService implements Resolve<IDeviceModel> {
  constructor(protected service: DeviceModelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeviceModel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deviceModel: HttpResponse<DeviceModel>) => {
          if (deviceModel.body) {
            return of(deviceModel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeviceModel());
  }
}

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOption, Option } from '../option.model';
import { OptionService } from '../service/option.service';

@Injectable({ providedIn: 'root' })
export class OptionRoutingResolveService implements Resolve<IOption> {
  constructor(protected service: OptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOption> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((option: HttpResponse<Option>) => {
          if (option.body) {
            return of(option.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Option());
  }
}

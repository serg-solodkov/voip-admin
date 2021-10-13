import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOptionValue, OptionValue } from '../option-value.model';
import { OptionValueService } from '../service/option-value.service';

@Injectable({ providedIn: 'root' })
export class OptionValueRoutingResolveService implements Resolve<IOptionValue> {
  constructor(protected service: OptionValueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOptionValue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((optionValue: HttpResponse<OptionValue>) => {
          if (optionValue.body) {
            return of(optionValue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OptionValue());
  }
}

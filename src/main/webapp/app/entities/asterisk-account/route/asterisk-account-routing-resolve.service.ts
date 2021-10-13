import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAsteriskAccount, AsteriskAccount } from '../asterisk-account.model';
import { AsteriskAccountService } from '../service/asterisk-account.service';

@Injectable({ providedIn: 'root' })
export class AsteriskAccountRoutingResolveService implements Resolve<IAsteriskAccount> {
  constructor(protected service: AsteriskAccountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAsteriskAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((asteriskAccount: HttpResponse<AsteriskAccount>) => {
          if (asteriskAccount.body) {
            return of(asteriskAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AsteriskAccount());
  }
}

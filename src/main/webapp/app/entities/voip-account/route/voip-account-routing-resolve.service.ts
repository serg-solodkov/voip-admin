import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVoipAccount, VoipAccount } from '../voip-account.model';
import { VoipAccountService } from '../service/voip-account.service';

@Injectable({ providedIn: 'root' })
export class VoipAccountRoutingResolveService implements Resolve<IVoipAccount> {
  constructor(protected service: VoipAccountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVoipAccount> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((voipAccount: HttpResponse<VoipAccount>) => {
          if (voipAccount.body) {
            return of(voipAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VoipAccount());
  }
}

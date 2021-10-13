import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResponsiblePerson, ResponsiblePerson } from '../responsible-person.model';
import { ResponsiblePersonService } from '../service/responsible-person.service';

@Injectable({ providedIn: 'root' })
export class ResponsiblePersonRoutingResolveService implements Resolve<IResponsiblePerson> {
  constructor(protected service: ResponsiblePersonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResponsiblePerson> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((responsiblePerson: HttpResponse<ResponsiblePerson>) => {
          if (responsiblePerson.body) {
            return of(responsiblePerson.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ResponsiblePerson());
  }
}

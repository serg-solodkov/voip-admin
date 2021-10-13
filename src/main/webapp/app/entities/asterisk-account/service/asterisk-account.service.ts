import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAsteriskAccount, getAsteriskAccountIdentifier } from '../asterisk-account.model';

export type EntityResponseType = HttpResponse<IAsteriskAccount>;
export type EntityArrayResponseType = HttpResponse<IAsteriskAccount[]>;

@Injectable({ providedIn: 'root' })
export class AsteriskAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asterisk-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(asteriskAccount: IAsteriskAccount): Observable<EntityResponseType> {
    return this.http.post<IAsteriskAccount>(this.resourceUrl, asteriskAccount, { observe: 'response' });
  }

  update(asteriskAccount: IAsteriskAccount): Observable<EntityResponseType> {
    return this.http.put<IAsteriskAccount>(
      `${this.resourceUrl}/${getAsteriskAccountIdentifier(asteriskAccount) as number}`,
      asteriskAccount,
      { observe: 'response' }
    );
  }

  partialUpdate(asteriskAccount: IAsteriskAccount): Observable<EntityResponseType> {
    return this.http.patch<IAsteriskAccount>(
      `${this.resourceUrl}/${getAsteriskAccountIdentifier(asteriskAccount) as number}`,
      asteriskAccount,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAsteriskAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAsteriskAccount[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAsteriskAccountToCollectionIfMissing(
    asteriskAccountCollection: IAsteriskAccount[],
    ...asteriskAccountsToCheck: (IAsteriskAccount | null | undefined)[]
  ): IAsteriskAccount[] {
    const asteriskAccounts: IAsteriskAccount[] = asteriskAccountsToCheck.filter(isPresent);
    if (asteriskAccounts.length > 0) {
      const asteriskAccountCollectionIdentifiers = asteriskAccountCollection.map(
        asteriskAccountItem => getAsteriskAccountIdentifier(asteriskAccountItem)!
      );
      const asteriskAccountsToAdd = asteriskAccounts.filter(asteriskAccountItem => {
        const asteriskAccountIdentifier = getAsteriskAccountIdentifier(asteriskAccountItem);
        if (asteriskAccountIdentifier == null || asteriskAccountCollectionIdentifiers.includes(asteriskAccountIdentifier)) {
          return false;
        }
        asteriskAccountCollectionIdentifiers.push(asteriskAccountIdentifier);
        return true;
      });
      return [...asteriskAccountsToAdd, ...asteriskAccountCollection];
    }
    return asteriskAccountCollection;
  }
}

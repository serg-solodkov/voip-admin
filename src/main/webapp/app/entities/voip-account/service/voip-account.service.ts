import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVoipAccount, getVoipAccountIdentifier } from '../voip-account.model';

export type EntityResponseType = HttpResponse<IVoipAccount>;
export type EntityArrayResponseType = HttpResponse<IVoipAccount[]>;

@Injectable({ providedIn: 'root' })
export class VoipAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/voip-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(voipAccount: IVoipAccount): Observable<EntityResponseType> {
    return this.http.post<IVoipAccount>(this.resourceUrl, voipAccount, { observe: 'response' });
  }

  update(voipAccount: IVoipAccount): Observable<EntityResponseType> {
    return this.http.put<IVoipAccount>(`${this.resourceUrl}/${getVoipAccountIdentifier(voipAccount) as number}`, voipAccount, {
      observe: 'response',
    });
  }

  partialUpdate(voipAccount: IVoipAccount): Observable<EntityResponseType> {
    return this.http.patch<IVoipAccount>(`${this.resourceUrl}/${getVoipAccountIdentifier(voipAccount) as number}`, voipAccount, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVoipAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVoipAccount[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVoipAccountToCollectionIfMissing(
    voipAccountCollection: IVoipAccount[],
    ...voipAccountsToCheck: (IVoipAccount | null | undefined)[]
  ): IVoipAccount[] {
    const voipAccounts: IVoipAccount[] = voipAccountsToCheck.filter(isPresent);
    if (voipAccounts.length > 0) {
      const voipAccountCollectionIdentifiers = voipAccountCollection.map(voipAccountItem => getVoipAccountIdentifier(voipAccountItem)!);
      const voipAccountsToAdd = voipAccounts.filter(voipAccountItem => {
        const voipAccountIdentifier = getVoipAccountIdentifier(voipAccountItem);
        if (voipAccountIdentifier == null || voipAccountCollectionIdentifiers.includes(voipAccountIdentifier)) {
          return false;
        }
        voipAccountCollectionIdentifiers.push(voipAccountIdentifier);
        return true;
      });
      return [...voipAccountsToAdd, ...voipAccountCollection];
    }
    return voipAccountCollection;
  }
}

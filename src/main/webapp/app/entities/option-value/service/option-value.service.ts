import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOptionValue, getOptionValueIdentifier } from '../option-value.model';

export type EntityResponseType = HttpResponse<IOptionValue>;
export type EntityArrayResponseType = HttpResponse<IOptionValue[]>;

@Injectable({ providedIn: 'root' })
export class OptionValueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/option-values');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(optionValue: IOptionValue): Observable<EntityResponseType> {
    return this.http.post<IOptionValue>(this.resourceUrl, optionValue, { observe: 'response' });
  }

  update(optionValue: IOptionValue): Observable<EntityResponseType> {
    return this.http.put<IOptionValue>(`${this.resourceUrl}/${getOptionValueIdentifier(optionValue) as number}`, optionValue, {
      observe: 'response',
    });
  }

  partialUpdate(optionValue: IOptionValue): Observable<EntityResponseType> {
    return this.http.patch<IOptionValue>(`${this.resourceUrl}/${getOptionValueIdentifier(optionValue) as number}`, optionValue, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOptionValue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOptionValue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOptionValueToCollectionIfMissing(
    optionValueCollection: IOptionValue[],
    ...optionValuesToCheck: (IOptionValue | null | undefined)[]
  ): IOptionValue[] {
    const optionValues: IOptionValue[] = optionValuesToCheck.filter(isPresent);
    if (optionValues.length > 0) {
      const optionValueCollectionIdentifiers = optionValueCollection.map(optionValueItem => getOptionValueIdentifier(optionValueItem)!);
      const optionValuesToAdd = optionValues.filter(optionValueItem => {
        const optionValueIdentifier = getOptionValueIdentifier(optionValueItem);
        if (optionValueIdentifier == null || optionValueCollectionIdentifiers.includes(optionValueIdentifier)) {
          return false;
        }
        optionValueCollectionIdentifiers.push(optionValueIdentifier);
        return true;
      });
      return [...optionValuesToAdd, ...optionValueCollection];
    }
    return optionValueCollection;
  }
}

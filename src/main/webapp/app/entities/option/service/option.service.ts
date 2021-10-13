import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOption, getOptionIdentifier } from '../option.model';

export type EntityResponseType = HttpResponse<IOption>;
export type EntityArrayResponseType = HttpResponse<IOption[]>;

@Injectable({ providedIn: 'root' })
export class OptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/options');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(option: IOption): Observable<EntityResponseType> {
    return this.http.post<IOption>(this.resourceUrl, option, { observe: 'response' });
  }

  update(option: IOption): Observable<EntityResponseType> {
    return this.http.put<IOption>(`${this.resourceUrl}/${getOptionIdentifier(option) as number}`, option, { observe: 'response' });
  }

  partialUpdate(option: IOption): Observable<EntityResponseType> {
    return this.http.patch<IOption>(`${this.resourceUrl}/${getOptionIdentifier(option) as number}`, option, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOption>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOption[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOptionToCollectionIfMissing(optionCollection: IOption[], ...optionsToCheck: (IOption | null | undefined)[]): IOption[] {
    const options: IOption[] = optionsToCheck.filter(isPresent);
    if (options.length > 0) {
      const optionCollectionIdentifiers = optionCollection.map(optionItem => getOptionIdentifier(optionItem)!);
      const optionsToAdd = options.filter(optionItem => {
        const optionIdentifier = getOptionIdentifier(optionItem);
        if (optionIdentifier == null || optionCollectionIdentifiers.includes(optionIdentifier)) {
          return false;
        }
        optionCollectionIdentifiers.push(optionIdentifier);
        return true;
      });
      return [...optionsToAdd, ...optionCollection];
    }
    return optionCollection;
  }
}

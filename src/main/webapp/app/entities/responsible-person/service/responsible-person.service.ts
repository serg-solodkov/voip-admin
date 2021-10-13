import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResponsiblePerson, getResponsiblePersonIdentifier } from '../responsible-person.model';

export type EntityResponseType = HttpResponse<IResponsiblePerson>;
export type EntityArrayResponseType = HttpResponse<IResponsiblePerson[]>;

@Injectable({ providedIn: 'root' })
export class ResponsiblePersonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/responsible-people');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(responsiblePerson: IResponsiblePerson): Observable<EntityResponseType> {
    return this.http.post<IResponsiblePerson>(this.resourceUrl, responsiblePerson, { observe: 'response' });
  }

  update(responsiblePerson: IResponsiblePerson): Observable<EntityResponseType> {
    return this.http.put<IResponsiblePerson>(
      `${this.resourceUrl}/${getResponsiblePersonIdentifier(responsiblePerson) as number}`,
      responsiblePerson,
      { observe: 'response' }
    );
  }

  partialUpdate(responsiblePerson: IResponsiblePerson): Observable<EntityResponseType> {
    return this.http.patch<IResponsiblePerson>(
      `${this.resourceUrl}/${getResponsiblePersonIdentifier(responsiblePerson) as number}`,
      responsiblePerson,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResponsiblePerson>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResponsiblePerson[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addResponsiblePersonToCollectionIfMissing(
    responsiblePersonCollection: IResponsiblePerson[],
    ...responsiblePeopleToCheck: (IResponsiblePerson | null | undefined)[]
  ): IResponsiblePerson[] {
    const responsiblePeople: IResponsiblePerson[] = responsiblePeopleToCheck.filter(isPresent);
    if (responsiblePeople.length > 0) {
      const responsiblePersonCollectionIdentifiers = responsiblePersonCollection.map(
        responsiblePersonItem => getResponsiblePersonIdentifier(responsiblePersonItem)!
      );
      const responsiblePeopleToAdd = responsiblePeople.filter(responsiblePersonItem => {
        const responsiblePersonIdentifier = getResponsiblePersonIdentifier(responsiblePersonItem);
        if (responsiblePersonIdentifier == null || responsiblePersonCollectionIdentifiers.includes(responsiblePersonIdentifier)) {
          return false;
        }
        responsiblePersonCollectionIdentifiers.push(responsiblePersonIdentifier);
        return true;
      });
      return [...responsiblePeopleToAdd, ...responsiblePersonCollection];
    }
    return responsiblePersonCollection;
  }
}

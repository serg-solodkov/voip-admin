import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOtherDeviceType, getOtherDeviceTypeIdentifier } from '../other-device-type.model';

export type EntityResponseType = HttpResponse<IOtherDeviceType>;
export type EntityArrayResponseType = HttpResponse<IOtherDeviceType[]>;

@Injectable({ providedIn: 'root' })
export class OtherDeviceTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/other-device-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(otherDeviceType: IOtherDeviceType): Observable<EntityResponseType> {
    return this.http.post<IOtherDeviceType>(this.resourceUrl, otherDeviceType, { observe: 'response' });
  }

  update(otherDeviceType: IOtherDeviceType): Observable<EntityResponseType> {
    return this.http.put<IOtherDeviceType>(
      `${this.resourceUrl}/${getOtherDeviceTypeIdentifier(otherDeviceType) as number}`,
      otherDeviceType,
      { observe: 'response' }
    );
  }

  partialUpdate(otherDeviceType: IOtherDeviceType): Observable<EntityResponseType> {
    return this.http.patch<IOtherDeviceType>(
      `${this.resourceUrl}/${getOtherDeviceTypeIdentifier(otherDeviceType) as number}`,
      otherDeviceType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOtherDeviceType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOtherDeviceType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOtherDeviceTypeToCollectionIfMissing(
    otherDeviceTypeCollection: IOtherDeviceType[],
    ...otherDeviceTypesToCheck: (IOtherDeviceType | null | undefined)[]
  ): IOtherDeviceType[] {
    const otherDeviceTypes: IOtherDeviceType[] = otherDeviceTypesToCheck.filter(isPresent);
    if (otherDeviceTypes.length > 0) {
      const otherDeviceTypeCollectionIdentifiers = otherDeviceTypeCollection.map(
        otherDeviceTypeItem => getOtherDeviceTypeIdentifier(otherDeviceTypeItem)!
      );
      const otherDeviceTypesToAdd = otherDeviceTypes.filter(otherDeviceTypeItem => {
        const otherDeviceTypeIdentifier = getOtherDeviceTypeIdentifier(otherDeviceTypeItem);
        if (otherDeviceTypeIdentifier == null || otherDeviceTypeCollectionIdentifiers.includes(otherDeviceTypeIdentifier)) {
          return false;
        }
        otherDeviceTypeCollectionIdentifiers.push(otherDeviceTypeIdentifier);
        return true;
      });
      return [...otherDeviceTypesToAdd, ...otherDeviceTypeCollection];
    }
    return otherDeviceTypeCollection;
  }
}

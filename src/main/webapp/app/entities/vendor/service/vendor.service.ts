import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVendor, getVendorIdentifier } from '../vendor.model';

export type EntityResponseType = HttpResponse<IVendor>;
export type EntityArrayResponseType = HttpResponse<IVendor[]>;

@Injectable({ providedIn: 'root' })
export class VendorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vendors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vendor: IVendor): Observable<EntityResponseType> {
    return this.http.post<IVendor>(this.resourceUrl, vendor, { observe: 'response' });
  }

  update(vendor: IVendor): Observable<EntityResponseType> {
    return this.http.put<IVendor>(`${this.resourceUrl}/${getVendorIdentifier(vendor) as number}`, vendor, { observe: 'response' });
  }

  partialUpdate(vendor: IVendor): Observable<EntityResponseType> {
    return this.http.patch<IVendor>(`${this.resourceUrl}/${getVendorIdentifier(vendor) as number}`, vendor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVendor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVendor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVendorToCollectionIfMissing(vendorCollection: IVendor[], ...vendorsToCheck: (IVendor | null | undefined)[]): IVendor[] {
    const vendors: IVendor[] = vendorsToCheck.filter(isPresent);
    if (vendors.length > 0) {
      const vendorCollectionIdentifiers = vendorCollection.map(vendorItem => getVendorIdentifier(vendorItem)!);
      const vendorsToAdd = vendors.filter(vendorItem => {
        const vendorIdentifier = getVendorIdentifier(vendorItem);
        if (vendorIdentifier == null || vendorCollectionIdentifiers.includes(vendorIdentifier)) {
          return false;
        }
        vendorCollectionIdentifiers.push(vendorIdentifier);
        return true;
      });
      return [...vendorsToAdd, ...vendorCollection];
    }
    return vendorCollection;
  }
}

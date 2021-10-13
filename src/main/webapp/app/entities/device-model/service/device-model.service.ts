import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeviceModel, getDeviceModelIdentifier } from '../device-model.model';

export type EntityResponseType = HttpResponse<IDeviceModel>;
export type EntityArrayResponseType = HttpResponse<IDeviceModel[]>;

@Injectable({ providedIn: 'root' })
export class DeviceModelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/device-models');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deviceModel: IDeviceModel): Observable<EntityResponseType> {
    return this.http.post<IDeviceModel>(this.resourceUrl, deviceModel, { observe: 'response' });
  }

  update(deviceModel: IDeviceModel): Observable<EntityResponseType> {
    return this.http.put<IDeviceModel>(`${this.resourceUrl}/${getDeviceModelIdentifier(deviceModel) as number}`, deviceModel, {
      observe: 'response',
    });
  }

  partialUpdate(deviceModel: IDeviceModel): Observable<EntityResponseType> {
    return this.http.patch<IDeviceModel>(`${this.resourceUrl}/${getDeviceModelIdentifier(deviceModel) as number}`, deviceModel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeviceModel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeviceModel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeviceModelToCollectionIfMissing(
    deviceModelCollection: IDeviceModel[],
    ...deviceModelsToCheck: (IDeviceModel | null | undefined)[]
  ): IDeviceModel[] {
    const deviceModels: IDeviceModel[] = deviceModelsToCheck.filter(isPresent);
    if (deviceModels.length > 0) {
      const deviceModelCollectionIdentifiers = deviceModelCollection.map(deviceModelItem => getDeviceModelIdentifier(deviceModelItem)!);
      const deviceModelsToAdd = deviceModels.filter(deviceModelItem => {
        const deviceModelIdentifier = getDeviceModelIdentifier(deviceModelItem);
        if (deviceModelIdentifier == null || deviceModelCollectionIdentifiers.includes(deviceModelIdentifier)) {
          return false;
        }
        deviceModelCollectionIdentifiers.push(deviceModelIdentifier);
        return true;
      });
      return [...deviceModelsToAdd, ...deviceModelCollection];
    }
    return deviceModelCollection;
  }
}

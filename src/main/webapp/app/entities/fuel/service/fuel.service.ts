import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFuel, getFuelIdentifier } from '../fuel.model';

export type EntityResponseType = HttpResponse<IFuel>;
export type EntityArrayResponseType = HttpResponse<IFuel[]>;

@Injectable({ providedIn: 'root' })
export class FuelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fuels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fuel: IFuel): Observable<EntityResponseType> {
    return this.http.post<IFuel>(this.resourceUrl, fuel, { observe: 'response' });
  }

  update(fuel: IFuel): Observable<EntityResponseType> {
    return this.http.put<IFuel>(`${this.resourceUrl}/${getFuelIdentifier(fuel) as number}`, fuel, { observe: 'response' });
  }

  partialUpdate(fuel: IFuel): Observable<EntityResponseType> {
    return this.http.patch<IFuel>(`${this.resourceUrl}/${getFuelIdentifier(fuel) as number}`, fuel, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFuel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFuel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFuelToCollectionIfMissing(fuelCollection: IFuel[], ...fuelsToCheck: (IFuel | null | undefined)[]): IFuel[] {
    const fuels: IFuel[] = fuelsToCheck.filter(isPresent);
    if (fuels.length > 0) {
      const fuelCollectionIdentifiers = fuelCollection.map(fuelItem => getFuelIdentifier(fuelItem)!);
      const fuelsToAdd = fuels.filter(fuelItem => {
        const fuelIdentifier = getFuelIdentifier(fuelItem);
        if (fuelIdentifier == null || fuelCollectionIdentifiers.includes(fuelIdentifier)) {
          return false;
        }
        fuelCollectionIdentifiers.push(fuelIdentifier);
        return true;
      });
      return [...fuelsToAdd, ...fuelCollection];
    }
    return fuelCollection;
  }
}

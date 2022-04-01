import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGearbox, getGearboxIdentifier } from '../gearbox.model';

export type EntityResponseType = HttpResponse<IGearbox>;
export type EntityArrayResponseType = HttpResponse<IGearbox[]>;

@Injectable({ providedIn: 'root' })
export class GearboxService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gearboxes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gearbox: IGearbox): Observable<EntityResponseType> {
    return this.http.post<IGearbox>(this.resourceUrl, gearbox, { observe: 'response' });
  }

  update(gearbox: IGearbox): Observable<EntityResponseType> {
    return this.http.put<IGearbox>(`${this.resourceUrl}/${getGearboxIdentifier(gearbox) as number}`, gearbox, { observe: 'response' });
  }

  partialUpdate(gearbox: IGearbox): Observable<EntityResponseType> {
    return this.http.patch<IGearbox>(`${this.resourceUrl}/${getGearboxIdentifier(gearbox) as number}`, gearbox, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGearbox>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGearbox[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGearboxToCollectionIfMissing(gearboxCollection: IGearbox[], ...gearboxesToCheck: (IGearbox | null | undefined)[]): IGearbox[] {
    const gearboxes: IGearbox[] = gearboxesToCheck.filter(isPresent);
    if (gearboxes.length > 0) {
      const gearboxCollectionIdentifiers = gearboxCollection.map(gearboxItem => getGearboxIdentifier(gearboxItem)!);
      const gearboxesToAdd = gearboxes.filter(gearboxItem => {
        const gearboxIdentifier = getGearboxIdentifier(gearboxItem);
        if (gearboxIdentifier == null || gearboxCollectionIdentifiers.includes(gearboxIdentifier)) {
          return false;
        }
        gearboxCollectionIdentifiers.push(gearboxIdentifier);
        return true;
      });
      return [...gearboxesToAdd, ...gearboxCollection];
    }
    return gearboxCollection;
  }
}

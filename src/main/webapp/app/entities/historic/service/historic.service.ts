import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHistoric, getHistoricIdentifier } from '../historic.model';

export type EntityResponseType = HttpResponse<IHistoric>;
export type EntityArrayResponseType = HttpResponse<IHistoric[]>;

@Injectable({ providedIn: 'root' })
export class HistoricService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/historics');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(historic: IHistoric): Observable<EntityResponseType> {
    return this.http.post<IHistoric>(this.resourceUrl, historic, { observe: 'response' });
  }

  update(historic: IHistoric): Observable<EntityResponseType> {
    return this.http.put<IHistoric>(`${this.resourceUrl}/${getHistoricIdentifier(historic) as number}`, historic, { observe: 'response' });
  }

  partialUpdate(historic: IHistoric): Observable<EntityResponseType> {
    return this.http.patch<IHistoric>(`${this.resourceUrl}/${getHistoricIdentifier(historic) as number}`, historic, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHistoric>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHistoric[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHistoricToCollectionIfMissing(historicCollection: IHistoric[], ...historicsToCheck: (IHistoric | null | undefined)[]): IHistoric[] {
    const historics: IHistoric[] = historicsToCheck.filter(isPresent);
    if (historics.length > 0) {
      const historicCollectionIdentifiers = historicCollection.map(historicItem => getHistoricIdentifier(historicItem)!);
      const historicsToAdd = historics.filter(historicItem => {
        const historicIdentifier = getHistoricIdentifier(historicItem);
        if (historicIdentifier == null || historicCollectionIdentifiers.includes(historicIdentifier)) {
          return false;
        }
        historicCollectionIdentifiers.push(historicIdentifier);
        return true;
      });
      return [...historicsToAdd, ...historicCollection];
    }
    return historicCollection;
  }
}

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHistoric, Historic } from '../historic.model';
import { HistoricService } from '../service/historic.service';

@Injectable({ providedIn: 'root' })
export class HistoricRoutingResolveService implements Resolve<IHistoric> {
  constructor(protected service: HistoricService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHistoric> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((historic: HttpResponse<Historic>) => {
          if (historic.body) {
            return of(historic.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Historic());
  }
}

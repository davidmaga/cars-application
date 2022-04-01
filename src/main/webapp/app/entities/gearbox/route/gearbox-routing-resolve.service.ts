import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGearbox, Gearbox } from '../gearbox.model';
import { GearboxService } from '../service/gearbox.service';

@Injectable({ providedIn: 'root' })
export class GearboxRoutingResolveService implements Resolve<IGearbox> {
  constructor(protected service: GearboxService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGearbox> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gearbox: HttpResponse<Gearbox>) => {
          if (gearbox.body) {
            return of(gearbox.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Gearbox());
  }
}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HistoricComponent } from '../list/historic.component';
import { HistoricDetailComponent } from '../detail/historic-detail.component';
import { HistoricUpdateComponent } from '../update/historic-update.component';
import { HistoricRoutingResolveService } from './historic-routing-resolve.service';

const historicRoute: Routes = [
  {
    path: '',
    component: HistoricComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HistoricDetailComponent,
    resolve: {
      historic: HistoricRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HistoricUpdateComponent,
    resolve: {
      historic: HistoricRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HistoricUpdateComponent,
    resolve: {
      historic: HistoricRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(historicRoute)],
  exports: [RouterModule],
})
export class HistoricRoutingModule {}

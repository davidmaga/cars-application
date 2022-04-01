import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GearboxComponent } from '../list/gearbox.component';
import { GearboxDetailComponent } from '../detail/gearbox-detail.component';
import { GearboxUpdateComponent } from '../update/gearbox-update.component';
import { GearboxRoutingResolveService } from './gearbox-routing-resolve.service';

const gearboxRoute: Routes = [
  {
    path: '',
    component: GearboxComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GearboxDetailComponent,
    resolve: {
      gearbox: GearboxRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GearboxUpdateComponent,
    resolve: {
      gearbox: GearboxRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GearboxUpdateComponent,
    resolve: {
      gearbox: GearboxRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gearboxRoute)],
  exports: [RouterModule],
})
export class GearboxRoutingModule {}

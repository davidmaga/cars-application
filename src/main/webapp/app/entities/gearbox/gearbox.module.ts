import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GearboxComponent } from './list/gearbox.component';
import { GearboxDetailComponent } from './detail/gearbox-detail.component';
import { GearboxUpdateComponent } from './update/gearbox-update.component';
import { GearboxDeleteDialogComponent } from './delete/gearbox-delete-dialog.component';
import { GearboxRoutingModule } from './route/gearbox-routing.module';

@NgModule({
  imports: [SharedModule, GearboxRoutingModule],
  declarations: [GearboxComponent, GearboxDetailComponent, GearboxUpdateComponent, GearboxDeleteDialogComponent],
  entryComponents: [GearboxDeleteDialogComponent],
})
export class GearboxModule {}

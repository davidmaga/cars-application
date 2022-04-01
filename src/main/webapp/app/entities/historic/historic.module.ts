import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HistoricComponent } from './list/historic.component';
import { HistoricDetailComponent } from './detail/historic-detail.component';
import { HistoricUpdateComponent } from './update/historic-update.component';
import { HistoricDeleteDialogComponent } from './delete/historic-delete-dialog.component';
import { HistoricRoutingModule } from './route/historic-routing.module';

@NgModule({
  imports: [SharedModule, HistoricRoutingModule],
  declarations: [HistoricComponent, HistoricDetailComponent, HistoricUpdateComponent, HistoricDeleteDialogComponent],
  entryComponents: [HistoricDeleteDialogComponent],
})
export class HistoricModule {}

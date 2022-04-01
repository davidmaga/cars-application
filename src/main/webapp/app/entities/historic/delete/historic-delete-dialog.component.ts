import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHistoric } from '../historic.model';
import { HistoricService } from '../service/historic.service';

@Component({
  templateUrl: './historic-delete-dialog.component.html',
})
export class HistoricDeleteDialogComponent {
  historic?: IHistoric;

  constructor(protected historicService: HistoricService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.historicService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

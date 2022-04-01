import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGearbox } from '../gearbox.model';
import { GearboxService } from '../service/gearbox.service';

@Component({
  templateUrl: './gearbox-delete-dialog.component.html',
})
export class GearboxDeleteDialogComponent {
  gearbox?: IGearbox;

  constructor(protected gearboxService: GearboxService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gearboxService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

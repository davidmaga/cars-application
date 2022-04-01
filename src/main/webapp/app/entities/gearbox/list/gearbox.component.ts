import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGearbox } from '../gearbox.model';
import { GearboxService } from '../service/gearbox.service';
import { GearboxDeleteDialogComponent } from '../delete/gearbox-delete-dialog.component';

@Component({
  selector: 'jhi-gearbox',
  templateUrl: './gearbox.component.html',
})
export class GearboxComponent implements OnInit {
  gearboxes?: IGearbox[];
  isLoading = false;

  constructor(protected gearboxService: GearboxService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.gearboxService.query().subscribe({
      next: (res: HttpResponse<IGearbox[]>) => {
        this.isLoading = false;
        this.gearboxes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IGearbox): number {
    return item.id!;
  }

  delete(gearbox: IGearbox): void {
    const modalRef = this.modalService.open(GearboxDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.gearbox = gearbox;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

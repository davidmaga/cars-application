import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHistoric } from '../historic.model';
import { HistoricService } from '../service/historic.service';
import { HistoricDeleteDialogComponent } from '../delete/historic-delete-dialog.component';

@Component({
  selector: 'jhi-historic',
  templateUrl: './historic.component.html',
})
export class HistoricComponent implements OnInit {
  historics?: IHistoric[];
  isLoading = false;

  constructor(protected historicService: HistoricService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.historicService.query().subscribe({
      next: (res: HttpResponse<IHistoric[]>) => {
        this.isLoading = false;
        this.historics = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IHistoric): number {
    return item.id!;
  }

  delete(historic: IHistoric): void {
    const modalRef = this.modalService.open(HistoricDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.historic = historic;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

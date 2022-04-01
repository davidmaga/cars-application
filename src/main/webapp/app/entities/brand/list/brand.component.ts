import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBrand } from '../brand.model';
import { BrandService } from '../service/brand.service';
import { BrandDeleteDialogComponent } from '../delete/brand-delete-dialog.component';

@Component({
  selector: 'jhi-brand',
  templateUrl: './brand.component.html',
})
export class BrandComponent implements OnInit {
  brands?: IBrand[];
  isLoading = false;

  constructor(protected brandService: BrandService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.brandService.query().subscribe({
      next: (res: HttpResponse<IBrand[]>) => {
        this.isLoading = false;
        this.brands = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBrand): number {
    return item.id!;
  }

  delete(brand: IBrand): void {
    const modalRef = this.modalService.open(BrandDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.brand = brand;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

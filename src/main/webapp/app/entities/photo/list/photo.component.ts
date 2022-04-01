import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPhoto } from '../photo.model';
import { PhotoService } from '../service/photo.service';
import { PhotoDeleteDialogComponent } from '../delete/photo-delete-dialog.component';

@Component({
  selector: 'jhi-photo',
  templateUrl: './photo.component.html',
})
export class PhotoComponent implements OnInit {
  photos?: IPhoto[];
  isLoading = false;

  constructor(protected photoService: PhotoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.photoService.query().subscribe({
      next: (res: HttpResponse<IPhoto[]>) => {
        this.isLoading = false;
        this.photos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPhoto): number {
    return item.id!;
  }

  delete(photo: IPhoto): void {
    const modalRef = this.modalService.open(PhotoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.photo = photo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPhoto, Photo } from '../photo.model';
import { PhotoService } from '../service/photo.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';

@Component({
  selector: 'jhi-photo-update',
  templateUrl: './photo-update.component.html',
})
export class PhotoUpdateComponent implements OnInit {
  isSaving = false;

  carsSharedCollection: ICar[] = [];

  editForm = this.fb.group({
    id: [],
    path: [],
    main: [],
    car: [],
  });

  constructor(
    protected photoService: PhotoService,
    protected carService: CarService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ photo }) => {
      this.updateForm(photo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const photo = this.createFromForm();
    if (photo.id !== undefined) {
      this.subscribeToSaveResponse(this.photoService.update(photo));
    } else {
      this.subscribeToSaveResponse(this.photoService.create(photo));
    }
  }

  trackCarById(index: number, item: ICar): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhoto>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(photo: IPhoto): void {
    this.editForm.patchValue({
      id: photo.id,
      path: photo.path,
      main: photo.main,
      car: photo.car,
    });

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing(this.carsSharedCollection, photo.car);
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing(cars, this.editForm.get('car')!.value)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));
  }

  protected createFromForm(): IPhoto {
    return {
      ...new Photo(),
      id: this.editForm.get(['id'])!.value,
      path: this.editForm.get(['path'])!.value,
      main: this.editForm.get(['main'])!.value,
      car: this.editForm.get(['car'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IHistoric, Historic } from '../historic.model';
import { HistoricService } from '../service/historic.service';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';

@Component({
  selector: 'jhi-historic-update',
  templateUrl: './historic-update.component.html',
})
export class HistoricUpdateComponent implements OnInit {
  isSaving = false;

  carsSharedCollection: ICar[] = [];

  editForm = this.fb.group({
    id: [],
    kms: [],
    price: [],
    car: [],
  });

  constructor(
    protected historicService: HistoricService,
    protected carService: CarService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historic }) => {
      this.updateForm(historic);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const historic = this.createFromForm();
    if (historic.id !== undefined) {
      this.subscribeToSaveResponse(this.historicService.update(historic));
    } else {
      this.subscribeToSaveResponse(this.historicService.create(historic));
    }
  }

  trackCarById(index: number, item: ICar): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHistoric>>): void {
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

  protected updateForm(historic: IHistoric): void {
    this.editForm.patchValue({
      id: historic.id,
      kms: historic.kms,
      price: historic.price,
      car: historic.car,
    });

    this.carsSharedCollection = this.carService.addCarToCollectionIfMissing(this.carsSharedCollection, historic.car);
  }

  protected loadRelationshipsOptions(): void {
    this.carService
      .query()
      .pipe(map((res: HttpResponse<ICar[]>) => res.body ?? []))
      .pipe(map((cars: ICar[]) => this.carService.addCarToCollectionIfMissing(cars, this.editForm.get('car')!.value)))
      .subscribe((cars: ICar[]) => (this.carsSharedCollection = cars));
  }

  protected createFromForm(): IHistoric {
    return {
      ...new Historic(),
      id: this.editForm.get(['id'])!.value,
      kms: this.editForm.get(['kms'])!.value,
      price: this.editForm.get(['price'])!.value,
      car: this.editForm.get(['car'])!.value,
    };
  }
}

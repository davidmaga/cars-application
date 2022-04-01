import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDealer, Dealer } from '../dealer.model';
import { DealerService } from '../service/dealer.service';
import { ICity } from 'app/entities/city/city.model';
import { CityService } from 'app/entities/city/service/city.service';

@Component({
  selector: 'jhi-dealer-update',
  templateUrl: './dealer-update.component.html',
})
export class DealerUpdateComponent implements OnInit {
  isSaving = false;

  citiesSharedCollection: ICity[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    photoPath: [],
    address: [],
    city: [],
  });

  constructor(
    protected dealerService: DealerService,
    protected cityService: CityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dealer }) => {
      this.updateForm(dealer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dealer = this.createFromForm();
    if (dealer.id !== undefined) {
      this.subscribeToSaveResponse(this.dealerService.update(dealer));
    } else {
      this.subscribeToSaveResponse(this.dealerService.create(dealer));
    }
  }

  trackCityById(index: number, item: ICity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDealer>>): void {
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

  protected updateForm(dealer: IDealer): void {
    this.editForm.patchValue({
      id: dealer.id,
      name: dealer.name,
      description: dealer.description,
      photoPath: dealer.photoPath,
      address: dealer.address,
      city: dealer.city,
    });

    this.citiesSharedCollection = this.cityService.addCityToCollectionIfMissing(this.citiesSharedCollection, dealer.city);
  }

  protected loadRelationshipsOptions(): void {
    this.cityService
      .query()
      .pipe(map((res: HttpResponse<ICity[]>) => res.body ?? []))
      .pipe(map((cities: ICity[]) => this.cityService.addCityToCollectionIfMissing(cities, this.editForm.get('city')!.value)))
      .subscribe((cities: ICity[]) => (this.citiesSharedCollection = cities));
  }

  protected createFromForm(): IDealer {
    return {
      ...new Dealer(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      photoPath: this.editForm.get(['photoPath'])!.value,
      address: this.editForm.get(['address'])!.value,
      city: this.editForm.get(['city'])!.value,
    };
  }
}

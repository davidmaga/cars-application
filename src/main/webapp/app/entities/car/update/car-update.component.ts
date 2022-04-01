import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICar, Car } from '../car.model';
import { CarService } from '../service/car.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IGearbox } from 'app/entities/gearbox/gearbox.model';
import { GearboxService } from 'app/entities/gearbox/service/gearbox.service';
import { IFuel } from 'app/entities/fuel/fuel.model';
import { FuelService } from 'app/entities/fuel/service/fuel.service';
import { IModel } from 'app/entities/model/model.model';
import { ModelService } from 'app/entities/model/service/model.service';
import { IDealer } from 'app/entities/dealer/dealer.model';
import { DealerService } from 'app/entities/dealer/service/dealer.service';

@Component({
  selector: 'jhi-car-update',
  templateUrl: './car-update.component.html',
})
export class CarUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategory[] = [];
  gearboxesSharedCollection: IGearbox[] = [];
  fuelsSharedCollection: IFuel[] = [];
  modelsSharedCollection: IModel[] = [];
  dealersSharedCollection: IDealer[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    engine: [],
    power: [],
    kms: [],
    color: [],
    doors: [],
    seats: [],
    buildingDate: [],
    price: [],
    offer: [],
    category: [],
    gearbox: [],
    fuel: [],
    model: [],
    dealer: [],
  });

  constructor(
    protected carService: CarService,
    protected categoryService: CategoryService,
    protected gearboxService: GearboxService,
    protected fuelService: FuelService,
    protected modelService: ModelService,
    protected dealerService: DealerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ car }) => {
      if (car.id === undefined) {
        const today = dayjs().startOf('day');
        car.buildingDate = today;
      }

      this.updateForm(car);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const car = this.createFromForm();
    if (car.id !== undefined) {
      this.subscribeToSaveResponse(this.carService.update(car));
    } else {
      this.subscribeToSaveResponse(this.carService.create(car));
    }
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  trackGearboxById(index: number, item: IGearbox): number {
    return item.id!;
  }

  trackFuelById(index: number, item: IFuel): number {
    return item.id!;
  }

  trackModelById(index: number, item: IModel): number {
    return item.id!;
  }

  trackDealerById(index: number, item: IDealer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICar>>): void {
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

  protected updateForm(car: ICar): void {
    this.editForm.patchValue({
      id: car.id,
      name: car.name,
      description: car.description,
      engine: car.engine,
      power: car.power,
      kms: car.kms,
      color: car.color,
      doors: car.doors,
      seats: car.seats,
      buildingDate: car.buildingDate ? car.buildingDate.format(DATE_TIME_FORMAT) : null,
      price: car.price,
      offer: car.offer,
      category: car.category,
      gearbox: car.gearbox,
      fuel: car.fuel,
      model: car.model,
      dealer: car.dealer,
    });

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(this.categoriesSharedCollection, car.category);
    this.gearboxesSharedCollection = this.gearboxService.addGearboxToCollectionIfMissing(this.gearboxesSharedCollection, car.gearbox);
    this.fuelsSharedCollection = this.fuelService.addFuelToCollectionIfMissing(this.fuelsSharedCollection, car.fuel);
    this.modelsSharedCollection = this.modelService.addModelToCollectionIfMissing(this.modelsSharedCollection, car.model);
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing(this.dealersSharedCollection, car.dealer);
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.gearboxService
      .query()
      .pipe(map((res: HttpResponse<IGearbox[]>) => res.body ?? []))
      .pipe(
        map((gearboxes: IGearbox[]) => this.gearboxService.addGearboxToCollectionIfMissing(gearboxes, this.editForm.get('gearbox')!.value))
      )
      .subscribe((gearboxes: IGearbox[]) => (this.gearboxesSharedCollection = gearboxes));

    this.fuelService
      .query()
      .pipe(map((res: HttpResponse<IFuel[]>) => res.body ?? []))
      .pipe(map((fuels: IFuel[]) => this.fuelService.addFuelToCollectionIfMissing(fuels, this.editForm.get('fuel')!.value)))
      .subscribe((fuels: IFuel[]) => (this.fuelsSharedCollection = fuels));

    this.modelService
      .query()
      .pipe(map((res: HttpResponse<IModel[]>) => res.body ?? []))
      .pipe(map((models: IModel[]) => this.modelService.addModelToCollectionIfMissing(models, this.editForm.get('model')!.value)))
      .subscribe((models: IModel[]) => (this.modelsSharedCollection = models));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing(dealers, this.editForm.get('dealer')!.value)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));
  }

  protected createFromForm(): ICar {
    return {
      ...new Car(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      engine: this.editForm.get(['engine'])!.value,
      power: this.editForm.get(['power'])!.value,
      kms: this.editForm.get(['kms'])!.value,
      color: this.editForm.get(['color'])!.value,
      doors: this.editForm.get(['doors'])!.value,
      seats: this.editForm.get(['seats'])!.value,
      buildingDate: this.editForm.get(['buildingDate'])!.value
        ? dayjs(this.editForm.get(['buildingDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      price: this.editForm.get(['price'])!.value,
      offer: this.editForm.get(['offer'])!.value,
      category: this.editForm.get(['category'])!.value,
      gearbox: this.editForm.get(['gearbox'])!.value,
      fuel: this.editForm.get(['fuel'])!.value,
      model: this.editForm.get(['model'])!.value,
      dealer: this.editForm.get(['dealer'])!.value,
    };
  }
}

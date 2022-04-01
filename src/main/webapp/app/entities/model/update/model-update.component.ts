import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IModel, Model } from '../model.model';
import { ModelService } from '../service/model.service';
import { IBrand } from 'app/entities/brand/brand.model';
import { BrandService } from 'app/entities/brand/service/brand.service';

@Component({
  selector: 'jhi-model-update',
  templateUrl: './model-update.component.html',
})
export class ModelUpdateComponent implements OnInit {
  isSaving = false;

  brandsSharedCollection: IBrand[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    brand: [],
  });

  constructor(
    protected modelService: ModelService,
    protected brandService: BrandService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ model }) => {
      this.updateForm(model);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const model = this.createFromForm();
    if (model.id !== undefined) {
      this.subscribeToSaveResponse(this.modelService.update(model));
    } else {
      this.subscribeToSaveResponse(this.modelService.create(model));
    }
  }

  trackBrandById(index: number, item: IBrand): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IModel>>): void {
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

  protected updateForm(model: IModel): void {
    this.editForm.patchValue({
      id: model.id,
      name: model.name,
      description: model.description,
      brand: model.brand,
    });

    this.brandsSharedCollection = this.brandService.addBrandToCollectionIfMissing(this.brandsSharedCollection, model.brand);
  }

  protected loadRelationshipsOptions(): void {
    this.brandService
      .query()
      .pipe(map((res: HttpResponse<IBrand[]>) => res.body ?? []))
      .pipe(map((brands: IBrand[]) => this.brandService.addBrandToCollectionIfMissing(brands, this.editForm.get('brand')!.value)))
      .subscribe((brands: IBrand[]) => (this.brandsSharedCollection = brands));
  }

  protected createFromForm(): IModel {
    return {
      ...new Model(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      brand: this.editForm.get(['brand'])!.value,
    };
  }
}

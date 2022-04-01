import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFuel, Fuel } from '../fuel.model';
import { FuelService } from '../service/fuel.service';

@Component({
  selector: 'jhi-fuel-update',
  templateUrl: './fuel-update.component.html',
})
export class FuelUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
  });

  constructor(protected fuelService: FuelService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fuel }) => {
      this.updateForm(fuel);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fuel = this.createFromForm();
    if (fuel.id !== undefined) {
      this.subscribeToSaveResponse(this.fuelService.update(fuel));
    } else {
      this.subscribeToSaveResponse(this.fuelService.create(fuel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFuel>>): void {
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

  protected updateForm(fuel: IFuel): void {
    this.editForm.patchValue({
      id: fuel.id,
      name: fuel.name,
      description: fuel.description,
    });
  }

  protected createFromForm(): IFuel {
    return {
      ...new Fuel(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}

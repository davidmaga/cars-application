import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IGearbox, Gearbox } from '../gearbox.model';
import { GearboxService } from '../service/gearbox.service';

@Component({
  selector: 'jhi-gearbox-update',
  templateUrl: './gearbox-update.component.html',
})
export class GearboxUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
  });

  constructor(protected gearboxService: GearboxService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gearbox }) => {
      this.updateForm(gearbox);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gearbox = this.createFromForm();
    if (gearbox.id !== undefined) {
      this.subscribeToSaveResponse(this.gearboxService.update(gearbox));
    } else {
      this.subscribeToSaveResponse(this.gearboxService.create(gearbox));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGearbox>>): void {
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

  protected updateForm(gearbox: IGearbox): void {
    this.editForm.patchValue({
      id: gearbox.id,
      name: gearbox.name,
      description: gearbox.description,
    });
  }

  protected createFromForm(): IGearbox {
    return {
      ...new Gearbox(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}

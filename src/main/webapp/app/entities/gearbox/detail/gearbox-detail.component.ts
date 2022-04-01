import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGearbox } from '../gearbox.model';

@Component({
  selector: 'jhi-gearbox-detail',
  templateUrl: './gearbox-detail.component.html',
})
export class GearboxDetailComponent implements OnInit {
  gearbox: IGearbox | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gearbox }) => {
      this.gearbox = gearbox;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

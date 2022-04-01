import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHistoric } from '../historic.model';

@Component({
  selector: 'jhi-historic-detail',
  templateUrl: './historic-detail.component.html',
})
export class HistoricDetailComponent implements OnInit {
  historic: IHistoric | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historic }) => {
      this.historic = historic;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

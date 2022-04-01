import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HistoricDetailComponent } from './historic-detail.component';

describe('Historic Management Detail Component', () => {
  let comp: HistoricDetailComponent;
  let fixture: ComponentFixture<HistoricDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HistoricDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ historic: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HistoricDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HistoricDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load historic on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.historic).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

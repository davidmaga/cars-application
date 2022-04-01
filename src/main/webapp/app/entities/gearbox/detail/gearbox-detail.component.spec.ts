import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GearboxDetailComponent } from './gearbox-detail.component';

describe('Gearbox Management Detail Component', () => {
  let comp: GearboxDetailComponent;
  let fixture: ComponentFixture<GearboxDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GearboxDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ gearbox: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GearboxDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GearboxDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load gearbox on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.gearbox).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

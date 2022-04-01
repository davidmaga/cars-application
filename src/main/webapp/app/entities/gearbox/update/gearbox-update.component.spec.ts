import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GearboxService } from '../service/gearbox.service';
import { IGearbox, Gearbox } from '../gearbox.model';

import { GearboxUpdateComponent } from './gearbox-update.component';

describe('Gearbox Management Update Component', () => {
  let comp: GearboxUpdateComponent;
  let fixture: ComponentFixture<GearboxUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gearboxService: GearboxService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GearboxUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(GearboxUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GearboxUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gearboxService = TestBed.inject(GearboxService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gearbox: IGearbox = { id: 456 };

      activatedRoute.data = of({ gearbox });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(gearbox));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gearbox>>();
      const gearbox = { id: 123 };
      jest.spyOn(gearboxService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gearbox });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gearbox }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(gearboxService.update).toHaveBeenCalledWith(gearbox);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gearbox>>();
      const gearbox = new Gearbox();
      jest.spyOn(gearboxService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gearbox });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gearbox }));
      saveSubject.complete();

      // THEN
      expect(gearboxService.create).toHaveBeenCalledWith(gearbox);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Gearbox>>();
      const gearbox = { id: 123 };
      jest.spyOn(gearboxService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gearbox });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gearboxService.update).toHaveBeenCalledWith(gearbox);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

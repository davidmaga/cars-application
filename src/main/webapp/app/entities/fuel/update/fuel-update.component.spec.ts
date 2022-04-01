import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FuelService } from '../service/fuel.service';
import { IFuel, Fuel } from '../fuel.model';

import { FuelUpdateComponent } from './fuel-update.component';

describe('Fuel Management Update Component', () => {
  let comp: FuelUpdateComponent;
  let fixture: ComponentFixture<FuelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fuelService: FuelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FuelUpdateComponent],
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
      .overrideTemplate(FuelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FuelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fuelService = TestBed.inject(FuelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fuel: IFuel = { id: 456 };

      activatedRoute.data = of({ fuel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fuel));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Fuel>>();
      const fuel = { id: 123 };
      jest.spyOn(fuelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fuel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fuel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fuelService.update).toHaveBeenCalledWith(fuel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Fuel>>();
      const fuel = new Fuel();
      jest.spyOn(fuelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fuel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fuel }));
      saveSubject.complete();

      // THEN
      expect(fuelService.create).toHaveBeenCalledWith(fuel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Fuel>>();
      const fuel = { id: 123 };
      jest.spyOn(fuelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fuel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fuelService.update).toHaveBeenCalledWith(fuel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

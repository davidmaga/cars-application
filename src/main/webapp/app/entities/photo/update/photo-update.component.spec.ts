import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PhotoService } from '../service/photo.service';
import { IPhoto, Photo } from '../photo.model';
import { ICar } from 'app/entities/car/car.model';
import { CarService } from 'app/entities/car/service/car.service';

import { PhotoUpdateComponent } from './photo-update.component';

describe('Photo Management Update Component', () => {
  let comp: PhotoUpdateComponent;
  let fixture: ComponentFixture<PhotoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let photoService: PhotoService;
  let carService: CarService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PhotoUpdateComponent],
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
      .overrideTemplate(PhotoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PhotoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    photoService = TestBed.inject(PhotoService);
    carService = TestBed.inject(CarService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Car query and add missing value', () => {
      const photo: IPhoto = { id: 456 };
      const car: ICar = { id: 94041 };
      photo.car = car;

      const carCollection: ICar[] = [{ id: 60031 }];
      jest.spyOn(carService, 'query').mockReturnValue(of(new HttpResponse({ body: carCollection })));
      const additionalCars = [car];
      const expectedCollection: ICar[] = [...additionalCars, ...carCollection];
      jest.spyOn(carService, 'addCarToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ photo });
      comp.ngOnInit();

      expect(carService.query).toHaveBeenCalled();
      expect(carService.addCarToCollectionIfMissing).toHaveBeenCalledWith(carCollection, ...additionalCars);
      expect(comp.carsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const photo: IPhoto = { id: 456 };
      const car: ICar = { id: 23995 };
      photo.car = car;

      activatedRoute.data = of({ photo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(photo));
      expect(comp.carsSharedCollection).toContain(car);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Photo>>();
      const photo = { id: 123 };
      jest.spyOn(photoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ photo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: photo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(photoService.update).toHaveBeenCalledWith(photo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Photo>>();
      const photo = new Photo();
      jest.spyOn(photoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ photo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: photo }));
      saveSubject.complete();

      // THEN
      expect(photoService.create).toHaveBeenCalledWith(photo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Photo>>();
      const photo = { id: 123 };
      jest.spyOn(photoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ photo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(photoService.update).toHaveBeenCalledWith(photo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCarById', () => {
      it('Should return tracked Car primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCarById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CarService } from '../service/car.service';
import { ICar, Car } from '../car.model';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IGearbox } from 'app/entities/gearbox/gearbox.model';
import { GearboxService } from 'app/entities/gearbox/service/gearbox.service';
import { IFuel } from 'app/entities/fuel/fuel.model';
import { FuelService } from 'app/entities/fuel/service/fuel.service';
import { IModel } from 'app/entities/model/model.model';
import { ModelService } from 'app/entities/model/service/model.service';
import { IDealer } from 'app/entities/dealer/dealer.model';
import { DealerService } from 'app/entities/dealer/service/dealer.service';

import { CarUpdateComponent } from './car-update.component';

describe('Car Management Update Component', () => {
  let comp: CarUpdateComponent;
  let fixture: ComponentFixture<CarUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let carService: CarService;
  let categoryService: CategoryService;
  let gearboxService: GearboxService;
  let fuelService: FuelService;
  let modelService: ModelService;
  let dealerService: DealerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CarUpdateComponent],
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
      .overrideTemplate(CarUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CarUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    carService = TestBed.inject(CarService);
    categoryService = TestBed.inject(CategoryService);
    gearboxService = TestBed.inject(GearboxService);
    fuelService = TestBed.inject(FuelService);
    modelService = TestBed.inject(ModelService);
    dealerService = TestBed.inject(DealerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Category query and add missing value', () => {
      const car: ICar = { id: 456 };
      const category: ICategory = { id: 66915 };
      car.category = category;

      const categoryCollection: ICategory[] = [{ id: 54854 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, ...additionalCategories);
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Gearbox query and add missing value', () => {
      const car: ICar = { id: 456 };
      const gearbox: IGearbox = { id: 1143 };
      car.gearbox = gearbox;

      const gearboxCollection: IGearbox[] = [{ id: 5150 }];
      jest.spyOn(gearboxService, 'query').mockReturnValue(of(new HttpResponse({ body: gearboxCollection })));
      const additionalGearboxes = [gearbox];
      const expectedCollection: IGearbox[] = [...additionalGearboxes, ...gearboxCollection];
      jest.spyOn(gearboxService, 'addGearboxToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(gearboxService.query).toHaveBeenCalled();
      expect(gearboxService.addGearboxToCollectionIfMissing).toHaveBeenCalledWith(gearboxCollection, ...additionalGearboxes);
      expect(comp.gearboxesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Fuel query and add missing value', () => {
      const car: ICar = { id: 456 };
      const fuel: IFuel = { id: 70902 };
      car.fuel = fuel;

      const fuelCollection: IFuel[] = [{ id: 96419 }];
      jest.spyOn(fuelService, 'query').mockReturnValue(of(new HttpResponse({ body: fuelCollection })));
      const additionalFuels = [fuel];
      const expectedCollection: IFuel[] = [...additionalFuels, ...fuelCollection];
      jest.spyOn(fuelService, 'addFuelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(fuelService.query).toHaveBeenCalled();
      expect(fuelService.addFuelToCollectionIfMissing).toHaveBeenCalledWith(fuelCollection, ...additionalFuels);
      expect(comp.fuelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Model query and add missing value', () => {
      const car: ICar = { id: 456 };
      const model: IModel = { id: 68452 };
      car.model = model;

      const modelCollection: IModel[] = [{ id: 47302 }];
      jest.spyOn(modelService, 'query').mockReturnValue(of(new HttpResponse({ body: modelCollection })));
      const additionalModels = [model];
      const expectedCollection: IModel[] = [...additionalModels, ...modelCollection];
      jest.spyOn(modelService, 'addModelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(modelService.query).toHaveBeenCalled();
      expect(modelService.addModelToCollectionIfMissing).toHaveBeenCalledWith(modelCollection, ...additionalModels);
      expect(comp.modelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dealer query and add missing value', () => {
      const car: ICar = { id: 456 };
      const dealer: IDealer = { id: 93774 };
      car.dealer = dealer;

      const dealerCollection: IDealer[] = [{ id: 41470 }];
      jest.spyOn(dealerService, 'query').mockReturnValue(of(new HttpResponse({ body: dealerCollection })));
      const additionalDealers = [dealer];
      const expectedCollection: IDealer[] = [...additionalDealers, ...dealerCollection];
      jest.spyOn(dealerService, 'addDealerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(dealerService.query).toHaveBeenCalled();
      expect(dealerService.addDealerToCollectionIfMissing).toHaveBeenCalledWith(dealerCollection, ...additionalDealers);
      expect(comp.dealersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const car: ICar = { id: 456 };
      const category: ICategory = { id: 87384 };
      car.category = category;
      const gearbox: IGearbox = { id: 97499 };
      car.gearbox = gearbox;
      const fuel: IFuel = { id: 86740 };
      car.fuel = fuel;
      const model: IModel = { id: 43621 };
      car.model = model;
      const dealer: IDealer = { id: 38725 };
      car.dealer = dealer;

      activatedRoute.data = of({ car });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(car));
      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.gearboxesSharedCollection).toContain(gearbox);
      expect(comp.fuelsSharedCollection).toContain(fuel);
      expect(comp.modelsSharedCollection).toContain(model);
      expect(comp.dealersSharedCollection).toContain(dealer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Car>>();
      const car = { id: 123 };
      jest.spyOn(carService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ car });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: car }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(carService.update).toHaveBeenCalledWith(car);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Car>>();
      const car = new Car();
      jest.spyOn(carService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ car });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: car }));
      saveSubject.complete();

      // THEN
      expect(carService.create).toHaveBeenCalledWith(car);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Car>>();
      const car = { id: 123 };
      jest.spyOn(carService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ car });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(carService.update).toHaveBeenCalledWith(car);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCategoryById', () => {
      it('Should return tracked Category primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackGearboxById', () => {
      it('Should return tracked Gearbox primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGearboxById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFuelById', () => {
      it('Should return tracked Fuel primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFuelById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackModelById', () => {
      it('Should return tracked Model primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackModelById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDealerById', () => {
      it('Should return tracked Dealer primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDealerById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});

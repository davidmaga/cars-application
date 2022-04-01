import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ModelService } from '../service/model.service';
import { IModel, Model } from '../model.model';
import { IBrand } from 'app/entities/brand/brand.model';
import { BrandService } from 'app/entities/brand/service/brand.service';

import { ModelUpdateComponent } from './model-update.component';

describe('Model Management Update Component', () => {
  let comp: ModelUpdateComponent;
  let fixture: ComponentFixture<ModelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let modelService: ModelService;
  let brandService: BrandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ModelUpdateComponent],
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
      .overrideTemplate(ModelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ModelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    modelService = TestBed.inject(ModelService);
    brandService = TestBed.inject(BrandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Brand query and add missing value', () => {
      const model: IModel = { id: 456 };
      const brand: IBrand = { id: 43245 };
      model.brand = brand;

      const brandCollection: IBrand[] = [{ id: 71919 }];
      jest.spyOn(brandService, 'query').mockReturnValue(of(new HttpResponse({ body: brandCollection })));
      const additionalBrands = [brand];
      const expectedCollection: IBrand[] = [...additionalBrands, ...brandCollection];
      jest.spyOn(brandService, 'addBrandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ model });
      comp.ngOnInit();

      expect(brandService.query).toHaveBeenCalled();
      expect(brandService.addBrandToCollectionIfMissing).toHaveBeenCalledWith(brandCollection, ...additionalBrands);
      expect(comp.brandsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const model: IModel = { id: 456 };
      const brand: IBrand = { id: 72913 };
      model.brand = brand;

      activatedRoute.data = of({ model });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(model));
      expect(comp.brandsSharedCollection).toContain(brand);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Model>>();
      const model = { id: 123 };
      jest.spyOn(modelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ model });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: model }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(modelService.update).toHaveBeenCalledWith(model);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Model>>();
      const model = new Model();
      jest.spyOn(modelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ model });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: model }));
      saveSubject.complete();

      // THEN
      expect(modelService.create).toHaveBeenCalledWith(model);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Model>>();
      const model = { id: 123 };
      jest.spyOn(modelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ model });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(modelService.update).toHaveBeenCalledWith(model);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBrandById', () => {
      it('Should return tracked Brand primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBrandById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});

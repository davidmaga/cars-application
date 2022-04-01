import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'car',
        data: { pageTitle: 'carsApp.car.home.title' },
        loadChildren: () => import('./car/car.module').then(m => m.CarModule),
      },
      {
        path: 'historic',
        data: { pageTitle: 'carsApp.historic.home.title' },
        loadChildren: () => import('./historic/historic.module').then(m => m.HistoricModule),
      },
      {
        path: 'photo',
        data: { pageTitle: 'carsApp.photo.home.title' },
        loadChildren: () => import('./photo/photo.module').then(m => m.PhotoModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'carsApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'brand',
        data: { pageTitle: 'carsApp.brand.home.title' },
        loadChildren: () => import('./brand/brand.module').then(m => m.BrandModule),
      },
      {
        path: 'model',
        data: { pageTitle: 'carsApp.model.home.title' },
        loadChildren: () => import('./model/model.module').then(m => m.ModelModule),
      },
      {
        path: 'fuel',
        data: { pageTitle: 'carsApp.fuel.home.title' },
        loadChildren: () => import('./fuel/fuel.module').then(m => m.FuelModule),
      },
      {
        path: 'gearbox',
        data: { pageTitle: 'carsApp.gearbox.home.title' },
        loadChildren: () => import('./gearbox/gearbox.module').then(m => m.GearboxModule),
      },
      {
        path: 'dealer',
        data: { pageTitle: 'carsApp.dealer.home.title' },
        loadChildren: () => import('./dealer/dealer.module').then(m => m.DealerModule),
      },
      {
        path: 'city',
        data: { pageTitle: 'carsApp.city.home.title' },
        loadChildren: () => import('./city/city.module').then(m => m.CityModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'carsApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

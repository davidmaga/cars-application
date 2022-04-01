import { IBrand } from 'app/entities/brand/brand.model';

export interface IModel {
  id?: number;
  name?: string | null;
  description?: string | null;
  brand?: IBrand | null;
}

export class Model implements IModel {
  constructor(public id?: number, public name?: string | null, public description?: string | null, public brand?: IBrand | null) {}
}

export function getModelIdentifier(model: IModel): number | undefined {
  return model.id;
}

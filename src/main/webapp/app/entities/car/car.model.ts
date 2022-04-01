import dayjs from 'dayjs/esm';
import { ICategory } from 'app/entities/category/category.model';
import { IGearbox } from 'app/entities/gearbox/gearbox.model';
import { IFuel } from 'app/entities/fuel/fuel.model';
import { IModel } from 'app/entities/model/model.model';
import { IDealer } from 'app/entities/dealer/dealer.model';

export interface ICar {
  id?: number;
  name?: string;
  description?: string | null;
  engine?: string | null;
  power?: number | null;
  kms?: number | null;
  color?: string | null;
  doors?: number | null;
  seats?: number | null;
  buildingDate?: dayjs.Dayjs | null;
  price?: number | null;
  offer?: boolean | null;
  category?: ICategory | null;
  gearbox?: IGearbox | null;
  fuel?: IFuel | null;
  model?: IModel | null;
  dealer?: IDealer | null;
}

export class Car implements ICar {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public engine?: string | null,
    public power?: number | null,
    public kms?: number | null,
    public color?: string | null,
    public doors?: number | null,
    public seats?: number | null,
    public buildingDate?: dayjs.Dayjs | null,
    public price?: number | null,
    public offer?: boolean | null,
    public category?: ICategory | null,
    public gearbox?: IGearbox | null,
    public fuel?: IFuel | null,
    public model?: IModel | null,
    public dealer?: IDealer | null
  ) {
    this.offer = this.offer ?? false;
  }
}

export function getCarIdentifier(car: ICar): number | undefined {
  return car.id;
}

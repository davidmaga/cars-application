import { IRegion } from 'app/entities/region/region.model';

export interface ICity {
  id?: number;
  name?: string | null;
  description?: string | null;
  region?: IRegion | null;
}

export class City implements ICity {
  constructor(public id?: number, public name?: string | null, public description?: string | null, public region?: IRegion | null) {}
}

export function getCityIdentifier(city: ICity): number | undefined {
  return city.id;
}

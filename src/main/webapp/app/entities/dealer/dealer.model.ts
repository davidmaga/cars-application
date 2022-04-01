import { ICity } from 'app/entities/city/city.model';

export interface IDealer {
  id?: number;
  name?: string | null;
  description?: string | null;
  photoPath?: string | null;
  address?: string | null;
  city?: ICity | null;
}

export class Dealer implements IDealer {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public photoPath?: string | null,
    public address?: string | null,
    public city?: ICity | null
  ) {}
}

export function getDealerIdentifier(dealer: IDealer): number | undefined {
  return dealer.id;
}

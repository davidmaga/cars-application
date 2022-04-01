import { ICar } from 'app/entities/car/car.model';

export interface IHistoric {
  id?: number;
  kms?: number | null;
  price?: number | null;
  car?: ICar | null;
}

export class Historic implements IHistoric {
  constructor(public id?: number, public kms?: number | null, public price?: number | null, public car?: ICar | null) {}
}

export function getHistoricIdentifier(historic: IHistoric): number | undefined {
  return historic.id;
}

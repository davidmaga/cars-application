import { ICar } from 'app/entities/car/car.model';

export interface IPhoto {
  id?: number;
  path?: string | null;
  main?: boolean | null;
  car?: ICar | null;
}

export class Photo implements IPhoto {
  constructor(public id?: number, public path?: string | null, public main?: boolean | null, public car?: ICar | null) {
    this.main = this.main ?? false;
  }
}

export function getPhotoIdentifier(photo: IPhoto): number | undefined {
  return photo.id;
}

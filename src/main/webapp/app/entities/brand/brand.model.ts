export interface IBrand {
  id?: number;
  name?: string | null;
  description?: string | null;
  photoPath?: string | null;
}

export class Brand implements IBrand {
  constructor(public id?: number, public name?: string | null, public description?: string | null, public photoPath?: string | null) {}
}

export function getBrandIdentifier(brand: IBrand): number | undefined {
  return brand.id;
}

export interface IFuel {
  id?: number;
  name?: string | null;
  description?: string | null;
}

export class Fuel implements IFuel {
  constructor(public id?: number, public name?: string | null, public description?: string | null) {}
}

export function getFuelIdentifier(fuel: IFuel): number | undefined {
  return fuel.id;
}

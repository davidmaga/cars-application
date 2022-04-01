export interface IGearbox {
  id?: number;
  name?: string | null;
  description?: string | null;
}

export class Gearbox implements IGearbox {
  constructor(public id?: number, public name?: string | null, public description?: string | null) {}
}

export function getGearboxIdentifier(gearbox: IGearbox): number | undefined {
  return gearbox.id;
}

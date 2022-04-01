export interface IRegion {
  id?: number;
  name?: string | null;
  description?: string | null;
}

export class Region implements IRegion {
  constructor(public id?: number, public name?: string | null, public description?: string | null) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}

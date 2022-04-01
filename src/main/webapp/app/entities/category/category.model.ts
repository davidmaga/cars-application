export interface ICategory {
  id?: number;
  name?: string | null;
  description?: string | null;
  photoPath?: string | null;
}

export class Category implements ICategory {
  constructor(public id?: number, public name?: string | null, public description?: string | null, public photoPath?: string | null) {}
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}

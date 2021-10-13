import { IOption } from 'app/entities/option/option.model';

export interface IVendor {
  id?: number;
  name?: string | null;
  options?: IOption[] | null;
}

export class Vendor implements IVendor {
  constructor(public id?: number, public name?: string | null, public options?: IOption[] | null) {}
}

export function getVendorIdentifier(vendor: IVendor): number | undefined {
  return vendor.id;
}

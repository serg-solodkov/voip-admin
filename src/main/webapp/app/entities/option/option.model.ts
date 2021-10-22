import { IOptionValue } from 'app/entities/option-value/option-value.model';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { IDeviceModel } from 'app/entities/device-model/device-model.model';
import { OptionValueType } from 'app/entities/enumerations/option-value-type.model';

export interface IOption {
  id?: number;
  code?: string | null;
  descr?: string | null;
  codeWithDescr?: string | null;
  valueType?: OptionValueType | null;
  multiple?: boolean | null;
  possibleValues?: IOptionValue[] | null;
  vendors?: IVendor[] | null;
  models?: IDeviceModel[] | null;
}

export class Option implements IOption {
  constructor(
    public id?: number,
    public code?: string | null,
    public descr?: string | null,
    public codeWithDescr?: string | null,
    public valueType?: OptionValueType | null,
    public multiple?: boolean | null,
    public possibleValues?: IOptionValue[] | null,
    public vendors?: IVendor[] | null,
    public models?: IDeviceModel[] | null
  ) {
    this.multiple = this.multiple ?? false;
  }
}

export function getOptionIdentifier(option: IOption): number | undefined {
  return option.id;
}

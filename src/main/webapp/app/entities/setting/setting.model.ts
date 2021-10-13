import { IOption } from 'app/entities/option/option.model';
import { IOptionValue } from 'app/entities/option-value/option-value.model';
import { IDevice } from 'app/entities/device/device.model';

export interface ISetting {
  id?: number;
  textValue?: string | null;
  option?: IOption | null;
  selectedValues?: IOptionValue[] | null;
  device?: IDevice | null;
}

export class Setting implements ISetting {
  constructor(
    public id?: number,
    public textValue?: string | null,
    public option?: IOption | null,
    public selectedValues?: IOptionValue[] | null,
    public device?: IDevice | null
  ) {}
}

export function getSettingIdentifier(setting: ISetting): number | undefined {
  return setting.id;
}

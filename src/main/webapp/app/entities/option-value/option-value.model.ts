import { IOption } from 'app/entities/option/option.model';
import { ISetting } from 'app/entities/setting/setting.model';

export interface IOptionValue {
  id?: number;
  value?: string | null;
  option?: IOption | null;
  settings?: ISetting[] | null;
}

export class OptionValue implements IOptionValue {
  constructor(public id?: number, public value?: string | null, public option?: IOption | null, public settings?: ISetting[] | null) {}
}

export function getOptionValueIdentifier(optionValue: IOptionValue): number | undefined {
  return optionValue.id;
}

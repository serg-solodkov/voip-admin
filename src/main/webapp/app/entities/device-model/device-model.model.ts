import { IOtherDeviceType } from 'app/entities/other-device-type/other-device-type.model';
import { IVendor } from 'app/entities/vendor/vendor.model';
import { IOption } from 'app/entities/option/option.model';
import { DeviceType } from 'app/entities/enumerations/device-type.model';

export interface IDeviceModel {
  id?: number;
  name?: string;
  isConfigurable?: boolean;
  linesCount?: number | null;
  configTemplateContentType?: string | null;
  configTemplate?: string | null;
  firmwareFileContentType?: string | null;
  firmwareFile?: string | null;
  deviceType?: DeviceType | null;
  otherDeviceType?: IOtherDeviceType | null;
  vendor?: IVendor | null;
  options?: IOption[] | null;
}

export class DeviceModel implements IDeviceModel {
  constructor(
    public id?: number,
    public name?: string,
    public isConfigurable?: boolean,
    public linesCount?: number | null,
    public configTemplateContentType?: string | null,
    public configTemplate?: string | null,
    public firmwareFileContentType?: string | null,
    public firmwareFile?: string | null,
    public deviceType?: DeviceType | null,
    public otherDeviceType?: IOtherDeviceType | null,
    public vendor?: IVendor | null,
    public options?: IOption[] | null
  ) {
    this.isConfigurable = this.isConfigurable ?? false;
  }
}

export function getDeviceModelIdentifier(deviceModel: IDeviceModel): number | undefined {
  return deviceModel.id;
}

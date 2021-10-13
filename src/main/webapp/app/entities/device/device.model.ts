import { ISetting } from 'app/entities/setting/setting.model';
import { IVoipAccount } from 'app/entities/voip-account/voip-account.model';
import { IDeviceModel } from 'app/entities/device-model/device-model.model';
import { IResponsiblePerson } from 'app/entities/responsible-person/responsible-person.model';
import { ProvisioningMode } from 'app/entities/enumerations/provisioning-mode.model';

export interface IDevice {
  id?: number;
  mac?: string;
  inventoryNumber?: string | null;
  location?: string | null;
  hostname?: string | null;
  webLogin?: string | null;
  webPassword?: string | null;
  dhcpEnabled?: boolean | null;
  ipAddress?: string | null;
  subnetMask?: string | null;
  defaultGw?: string | null;
  dns1?: string | null;
  dns2?: string | null;
  provisioningMode?: ProvisioningMode | null;
  provisioningUrl?: string | null;
  ntpServer?: string | null;
  notes?: string | null;
  configurationContentType?: string | null;
  configuration?: string | null;
  settings?: ISetting[] | null;
  voipAccounts?: IVoipAccount[] | null;
  children?: IDevice[] | null;
  model?: IDeviceModel | null;
  responsiblePerson?: IResponsiblePerson | null;
  parent?: IDevice | null;
}

export class Device implements IDevice {
  constructor(
    public id?: number,
    public mac?: string,
    public inventoryNumber?: string | null,
    public location?: string | null,
    public hostname?: string | null,
    public webLogin?: string | null,
    public webPassword?: string | null,
    public dhcpEnabled?: boolean | null,
    public ipAddress?: string | null,
    public subnetMask?: string | null,
    public defaultGw?: string | null,
    public dns1?: string | null,
    public dns2?: string | null,
    public provisioningMode?: ProvisioningMode | null,
    public provisioningUrl?: string | null,
    public ntpServer?: string | null,
    public notes?: string | null,
    public configurationContentType?: string | null,
    public configuration?: string | null,
    public settings?: ISetting[] | null,
    public voipAccounts?: IVoipAccount[] | null,
    public children?: IDevice[] | null,
    public model?: IDeviceModel | null,
    public responsiblePerson?: IResponsiblePerson | null,
    public parent?: IDevice | null
  ) {
    this.dhcpEnabled = this.dhcpEnabled ?? false;
  }
}

export function getDeviceIdentifier(device: IDevice): number | undefined {
  return device.id;
}

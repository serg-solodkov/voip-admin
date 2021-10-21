import { IAsteriskAccount } from 'app/entities/asterisk-account/asterisk-account.model';
import { IDevice } from 'app/entities/device/device.model';

export interface IVoipAccount {
  id?: number;
  manuallyCreated?: boolean | null;
  username?: string;
  password?: string | null;
  sipServer?: string | null;
  sipPort?: string | null;
  lineEnable?: boolean | null;
  lineNumber?: number | null;
  asteriskAccount?: IAsteriskAccount | null;
  device?: IDevice | null;
}

export class VoipAccount implements IVoipAccount {
  constructor(
    public id?: number,
    public manuallyCreated?: boolean | null,
    public username?: string,
    public password?: string | null,
    public sipServer?: string | null,
    public sipPort?: string | null,
    public lineEnable?: boolean | null,
    public lineNumber?: number | null,
    public asteriskAccount?: IAsteriskAccount | null,
    public device?: IDevice | null
  ) {
    this.manuallyCreated = this.manuallyCreated ?? false;
    this.lineEnable = this.lineEnable ?? false;
  }
}

export function getVoipAccountIdentifier(voipAccount: IVoipAccount): number | undefined {
  return voipAccount.id;
}

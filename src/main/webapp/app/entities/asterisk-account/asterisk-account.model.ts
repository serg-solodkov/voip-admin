import { IVoipAccount } from 'app/entities/voip-account/voip-account.model';

export interface IAsteriskAccount {
  id?: number;
  username?: string | null;
  asteriskId?: string | null;
  voipAccount?: IVoipAccount | null;
}

export class AsteriskAccount implements IAsteriskAccount {
  constructor(
    public id?: number,
    public username?: string | null,
    public asteriskId?: string | null,
    public voipAccount?: IVoipAccount | null
  ) {}
}

export function getAsteriskAccountIdentifier(asteriskAccount: IAsteriskAccount): number | undefined {
  return asteriskAccount.id;
}

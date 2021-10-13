export interface IOtherDeviceType {
  id?: number;
  name?: string | null;
  description?: string | null;
}

export class OtherDeviceType implements IOtherDeviceType {
  constructor(public id?: number, public name?: string | null, public description?: string | null) {}
}

export function getOtherDeviceTypeIdentifier(otherDeviceType: IOtherDeviceType): number | undefined {
  return otherDeviceType.id;
}

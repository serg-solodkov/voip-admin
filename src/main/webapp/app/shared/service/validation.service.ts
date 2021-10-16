import { Injectable } from '@angular/core';
import { FormControl } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class ValidationService {
  ipAddressOrDomainNamePattern(control: FormControl): { [key: string]: any } | null {
    const nonDigitsPattern = new RegExp('[^0-9.]');
    const ipAddressPattern = new RegExp('^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$');
    const domainNamePattern = new RegExp('(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9]');

    const str = control.value;
    if (!str) {
      return null;
    }
    if (nonDigitsPattern.test(str)) {
      // if str contains any letters, test it for domain-name pattern
      return !domainNamePattern.test(str) ? { wrongDomainName: true } : null;
    } else {
      // else test it for ip-address
      return !ipAddressPattern.test(str) ? { wrongIpAddress: true } : null;
    }
  }
}

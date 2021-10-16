import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'firstLetter'
})
export class FirstLetterPipe implements PipeTransform {

  transform(value: string | null | undefined, ...args: unknown[]): string | null {
    return !!value && value.length > 0 ? value.substr(0, 1) + '.' : null;
  }

}

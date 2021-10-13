import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOtherDeviceType } from '../other-device-type.model';

@Component({
  selector: 'jhi-other-device-type-detail',
  templateUrl: './other-device-type-detail.component.html',
})
export class OtherDeviceTypeDetailComponent implements OnInit {
  otherDeviceType: IOtherDeviceType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ otherDeviceType }) => {
      this.otherDeviceType = otherDeviceType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

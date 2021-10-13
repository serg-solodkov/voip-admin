import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeviceModel } from '../device-model.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-device-model-detail',
  templateUrl: './device-model-detail.component.html',
})
export class DeviceModelDetailComponent implements OnInit {
  deviceModel: IDeviceModel | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceModel }) => {
      this.deviceModel = deviceModel;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}

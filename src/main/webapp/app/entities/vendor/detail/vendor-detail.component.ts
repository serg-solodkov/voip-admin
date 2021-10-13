import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVendor } from '../vendor.model';

@Component({
  selector: 'jhi-vendor-detail',
  templateUrl: './vendor-detail.component.html',
})
export class VendorDetailComponent implements OnInit {
  vendor: IVendor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendor }) => {
      this.vendor = vendor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

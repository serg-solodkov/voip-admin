import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VendorComponent } from '../list/vendor.component';
import { VendorDetailComponent } from '../detail/vendor-detail.component';
import { VendorUpdateComponent } from '../update/vendor-update.component';
import { VendorRoutingResolveService } from './vendor-routing-resolve.service';

const vendorRoute: Routes = [
  {
    path: '',
    component: VendorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VendorDetailComponent,
    resolve: {
      vendor: VendorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VendorUpdateComponent,
    resolve: {
      vendor: VendorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VendorUpdateComponent,
    resolve: {
      vendor: VendorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vendorRoute)],
  exports: [RouterModule],
})
export class VendorRoutingModule {}

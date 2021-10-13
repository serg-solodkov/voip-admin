import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OtherDeviceTypeComponent } from '../list/other-device-type.component';
import { OtherDeviceTypeDetailComponent } from '../detail/other-device-type-detail.component';
import { OtherDeviceTypeUpdateComponent } from '../update/other-device-type-update.component';
import { OtherDeviceTypeRoutingResolveService } from './other-device-type-routing-resolve.service';

const otherDeviceTypeRoute: Routes = [
  {
    path: '',
    component: OtherDeviceTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OtherDeviceTypeDetailComponent,
    resolve: {
      otherDeviceType: OtherDeviceTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OtherDeviceTypeUpdateComponent,
    resolve: {
      otherDeviceType: OtherDeviceTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OtherDeviceTypeUpdateComponent,
    resolve: {
      otherDeviceType: OtherDeviceTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(otherDeviceTypeRoute)],
  exports: [RouterModule],
})
export class OtherDeviceTypeRoutingModule {}

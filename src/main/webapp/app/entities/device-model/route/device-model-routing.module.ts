import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeviceModelComponent } from '../list/device-model.component';
import { DeviceModelDetailComponent } from '../detail/device-model-detail.component';
import { DeviceModelUpdateComponent } from '../update/device-model-update.component';
import { DeviceModelRoutingResolveService } from './device-model-routing-resolve.service';

const deviceModelRoute: Routes = [
  {
    path: '',
    component: DeviceModelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeviceModelDetailComponent,
    resolve: {
      deviceModel: DeviceModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeviceModelUpdateComponent,
    resolve: {
      deviceModel: DeviceModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeviceModelUpdateComponent,
    resolve: {
      deviceModel: DeviceModelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deviceModelRoute)],
  exports: [RouterModule],
})
export class DeviceModelRoutingModule {}

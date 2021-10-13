import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AsteriskAccountComponent } from '../list/asterisk-account.component';
import { AsteriskAccountDetailComponent } from '../detail/asterisk-account-detail.component';
import { AsteriskAccountUpdateComponent } from '../update/asterisk-account-update.component';
import { AsteriskAccountRoutingResolveService } from './asterisk-account-routing-resolve.service';

const asteriskAccountRoute: Routes = [
  {
    path: '',
    component: AsteriskAccountComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AsteriskAccountDetailComponent,
    resolve: {
      asteriskAccount: AsteriskAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AsteriskAccountUpdateComponent,
    resolve: {
      asteriskAccount: AsteriskAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AsteriskAccountUpdateComponent,
    resolve: {
      asteriskAccount: AsteriskAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(asteriskAccountRoute)],
  exports: [RouterModule],
})
export class AsteriskAccountRoutingModule {}

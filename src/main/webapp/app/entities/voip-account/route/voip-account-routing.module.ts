import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VoipAccountComponent } from '../list/voip-account.component';
import { VoipAccountDetailComponent } from '../detail/voip-account-detail.component';
import { VoipAccountUpdateComponent } from '../update/voip-account-update.component';
import { VoipAccountRoutingResolveService } from './voip-account-routing-resolve.service';

const voipAccountRoute: Routes = [
  {
    path: '',
    component: VoipAccountComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VoipAccountDetailComponent,
    resolve: {
      voipAccount: VoipAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VoipAccountUpdateComponent,
    resolve: {
      voipAccount: VoipAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VoipAccountUpdateComponent,
    resolve: {
      voipAccount: VoipAccountRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(voipAccountRoute)],
  exports: [RouterModule],
})
export class VoipAccountRoutingModule {}

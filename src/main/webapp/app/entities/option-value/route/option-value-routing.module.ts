import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OptionValueComponent } from '../list/option-value.component';
import { OptionValueDetailComponent } from '../detail/option-value-detail.component';
import { OptionValueUpdateComponent } from '../update/option-value-update.component';
import { OptionValueRoutingResolveService } from './option-value-routing-resolve.service';

const optionValueRoute: Routes = [
  {
    path: '',
    component: OptionValueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OptionValueDetailComponent,
    resolve: {
      optionValue: OptionValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OptionValueUpdateComponent,
    resolve: {
      optionValue: OptionValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OptionValueUpdateComponent,
    resolve: {
      optionValue: OptionValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(optionValueRoute)],
  exports: [RouterModule],
})
export class OptionValueRoutingModule {}

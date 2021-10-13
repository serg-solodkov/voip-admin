import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResponsiblePersonComponent } from '../list/responsible-person.component';
import { ResponsiblePersonDetailComponent } from '../detail/responsible-person-detail.component';
import { ResponsiblePersonUpdateComponent } from '../update/responsible-person-update.component';
import { ResponsiblePersonRoutingResolveService } from './responsible-person-routing-resolve.service';

const responsiblePersonRoute: Routes = [
  {
    path: '',
    component: ResponsiblePersonComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResponsiblePersonDetailComponent,
    resolve: {
      responsiblePerson: ResponsiblePersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResponsiblePersonUpdateComponent,
    resolve: {
      responsiblePerson: ResponsiblePersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResponsiblePersonUpdateComponent,
    resolve: {
      responsiblePerson: ResponsiblePersonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(responsiblePersonRoute)],
  exports: [RouterModule],
})
export class ResponsiblePersonRoutingModule {}

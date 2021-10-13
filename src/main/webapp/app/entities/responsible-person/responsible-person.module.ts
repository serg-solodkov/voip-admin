import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ResponsiblePersonComponent } from './list/responsible-person.component';
import { ResponsiblePersonDetailComponent } from './detail/responsible-person-detail.component';
import { ResponsiblePersonUpdateComponent } from './update/responsible-person-update.component';
import { ResponsiblePersonDeleteDialogComponent } from './delete/responsible-person-delete-dialog.component';
import { ResponsiblePersonRoutingModule } from './route/responsible-person-routing.module';

@NgModule({
  imports: [SharedModule, ResponsiblePersonRoutingModule],
  declarations: [
    ResponsiblePersonComponent,
    ResponsiblePersonDetailComponent,
    ResponsiblePersonUpdateComponent,
    ResponsiblePersonDeleteDialogComponent,
  ],
  entryComponents: [ResponsiblePersonDeleteDialogComponent],
})
export class ResponsiblePersonModule {}

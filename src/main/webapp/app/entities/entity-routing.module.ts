import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'device',
        data: { pageTitle: 'voipadminApp.device.home.title' },
        loadChildren: () => import('./device/device.module').then(m => m.DeviceModule),
      },
      {
        path: 'device-model',
        data: { pageTitle: 'voipadminApp.deviceModel.home.title' },
        loadChildren: () => import('./device-model/device-model.module').then(m => m.DeviceModelModule),
      },
      {
        path: 'other-device-type',
        data: { pageTitle: 'voipadminApp.otherDeviceType.home.title' },
        loadChildren: () => import('./other-device-type/other-device-type.module').then(m => m.OtherDeviceTypeModule),
      },
      {
        path: 'responsible-person',
        data: { pageTitle: 'voipadminApp.responsiblePerson.home.title' },
        loadChildren: () => import('./responsible-person/responsible-person.module').then(m => m.ResponsiblePersonModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'voipadminApp.department.home.title' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'voip-account',
        data: { pageTitle: 'voipadminApp.voipAccount.home.title' },
        loadChildren: () => import('./voip-account/voip-account.module').then(m => m.VoipAccountModule),
      },
      {
        path: 'asterisk-account',
        data: { pageTitle: 'voipadminApp.asteriskAccount.home.title' },
        loadChildren: () => import('./asterisk-account/asterisk-account.module').then(m => m.AsteriskAccountModule),
      },
      {
        path: 'setting',
        data: { pageTitle: 'voipadminApp.setting.home.title' },
        loadChildren: () => import('./setting/setting.module').then(m => m.SettingModule),
      },
      {
        path: 'option',
        data: { pageTitle: 'voipadminApp.option.home.title' },
        loadChildren: () => import('./option/option.module').then(m => m.OptionModule),
      },
      {
        path: 'option-value',
        data: { pageTitle: 'voipadminApp.optionValue.home.title' },
        loadChildren: () => import('./option-value/option-value.module').then(m => m.OptionValueModule),
      },
      {
        path: 'vendor',
        data: { pageTitle: 'voipadminApp.vendor.home.title' },
        loadChildren: () => import('./vendor/vendor.module').then(m => m.VendorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

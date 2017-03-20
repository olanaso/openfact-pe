import { RouterModule, Routes } from '@angular/router';

import { AdminComponent } from './admin.component';
import { NgModule } from '@angular/core';
import { OrganizationCreateComponent } from './organization-create/organization-create.component';
import { OrganizationListComponent } from './organization-list/organization-list.component';
import { ServerInfoComponent } from './server-info/server-info.component';

const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    children: [
      {
        path: 'organizations',
        component: OrganizationListComponent
      },
      {
        path: 'organizations/create',
        component: OrganizationCreateComponent
      },
      {
        path: 'server-info',
        component: ServerInfoComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class AdminRoutingModule { }

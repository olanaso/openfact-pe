import { NgModule } from '@angular/core';
import { AdminRoutingModule } from './admin-routing.module';
import { SharedModule } from '../shared/shared.module';

import { AdminComponent } from './admin.component';
import { AdminHeaderComponent } from './admin-header/admin-header.component';
import { OrganizationListComponent } from './organization-list/organization-list.component';
import { OrganizationCreateComponent } from './organization-create/organization-create.component';
import { ServerInfoComponent } from './server-info/server-info.component';

@NgModule({
  imports: [
    AdminRoutingModule,
    SharedModule,
  ],
  declarations: [
    AdminComponent,
    AdminHeaderComponent,
    OrganizationListComponent,
    OrganizationCreateComponent,
    ServerInfoComponent,
  ]
})
export class AdminModule { }

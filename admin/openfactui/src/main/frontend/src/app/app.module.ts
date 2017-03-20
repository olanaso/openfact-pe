import { TranslateLoader, TranslateModule, TranslateStaticLoader } from 'ng2-translate';

import { AboutComponent } from './pages/about/about.component';
import { AdminModule } from './admin/admin.module';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { CoreModule } from './core/core.module';
import { Http } from '@angular/http';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { OrganizationModule } from './organization/organization.module';
import { SharedModule } from './shared/shared.module';

export const BASE_URL = 'http://localhost:8080/openfact/rest';

export function createTranslateLoader(http: Http) {
  return new TranslateStaticLoader(http, './assets/i18n', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    AboutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,

    NgbModule.forRoot(),
    TranslateModule.forRoot({
      provide: TranslateLoader,
      useFactory: (createTranslateLoader),
      deps: [Http]
    }),

    SharedModule,
    CoreModule,
    AdminModule,
    OrganizationModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

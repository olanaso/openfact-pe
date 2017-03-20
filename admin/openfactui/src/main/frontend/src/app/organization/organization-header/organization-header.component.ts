import { ActivatedRoute, Router } from '@angular/router';
import { Component, Input, OnInit } from '@angular/core';

import { KeycloakService } from './../../core/keycloak.service';
import { Organization } from './../../core/model/organization.model';
import { TranslateService } from 'ng2-translate';

@Component({
  selector: 'of-organization-header',
  templateUrl: './organization-header.component.html',
  styles: []
})
export class OrganizationHeaderComponent implements OnInit {

  @Input()
  currentOrganization: Organization;

  @Input()
  organizations: Array<Organization>;

  authz: any;
  user: any = {
    username: ''
  };

  selectedLanguage: string;
  supportedLanguages: string[];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translate: TranslateService) { }

  ngOnInit() {
    this.authz = KeycloakService.auth.authz;
    this.user.username = this.authz.tokenParsed.username;

    if (this.currentOrganization.internationalizationEnabled === true) {
      this.changeLanguage(this.currentOrganization.defaultLocale || this.translate.currentLang || 'en');
    } else {
      this.changeLanguage(this.translate.getDefaultLang());
    }
    this.supportedLanguages = this.translate.getLangs();
  }

  changeLanguage(language: string) {
    this.translate.use(language);
    this.selectedLanguage = language;
  }

  accountManagement() {
    this.authz.accountManagement();
  }

  logout() {
    this.authz.logout();
  }

  about() {
    const url = this.router.createUrlTree(['./', { outlets: { secondary: 'about' } }]);
    this.router.navigateByUrl(url, { relativeTo: this.route });
  }

}

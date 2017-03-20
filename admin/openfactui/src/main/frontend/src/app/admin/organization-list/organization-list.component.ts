import { Component, OnInit } from '@angular/core';

import { DataService } from './../../core/data/data.service';
import { Organization } from './../../core/model/organization.model';
import { Router } from '@angular/router';

@Component({
  selector: 'of-organization-list',
  templateUrl: './organization-list.component.html',
  styleUrls: [],
  styles: [`
    .pull-right dropdown-kebab-pf {
      margin-top: 5px;
    }
  `]
})
export class OrganizationListComponent implements OnInit {

  searchResult: Array<Organization> = new Array<Organization>();

  filters: any = {
    filterText: undefined
  };

  constructor(
    private router: Router,
    private dataService: DataService) { }

  ngOnInit() {
    this.search();
  }

  search(): void {
    this.dataService.organizations().getAll().subscribe(
      (data) => {
        this.searchResult = data;
      }
    );
  }

  editOrganization(organization: Organization): void {
    this.router.navigate(['/organizations', organization.organization]);
  }

}

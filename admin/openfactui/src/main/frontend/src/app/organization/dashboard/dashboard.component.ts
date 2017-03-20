import { Component, OnDestroy, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { DataService } from './../../core/data/data.service';
import { Organization } from './../../core/model/organization.model';
import { Subscription } from 'rxjs/Subscription';
import { URLSearchParams } from '@angular/http';

@Component({
  selector: 'of-dashboard',
  templateUrl: './dashboard.component.html',
  styles: []
})
export class DashboardComponent implements OnInit, OnDestroy {

  private dataSubscription: Subscription;
  private organization: Organization;

  private providers;
  private jobReports: any[];

  constructor(
    private activatedRoute: ActivatedRoute,
    private dataService: DataService
  ) { }

  ngOnInit() {
    this.dataSubscription = this.activatedRoute.data.subscribe(data => {
      this.organization = data['organization'];
      this.refreshData();
    });
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  refreshData() {
    this.organization.getTaskProviders().subscribe(data1 => {
      this.providers = data1;

      const queryParams: URLSearchParams = new URLSearchParams();
      queryParams.set('jobName', this.providers);
      queryParams.set('first', '0');
      queryParams.set('max', this.providers.length);

      this.organization.getAllTasks(queryParams).subscribe(data2 => {
        this.jobReports = data2;
      });
    });
  }

}

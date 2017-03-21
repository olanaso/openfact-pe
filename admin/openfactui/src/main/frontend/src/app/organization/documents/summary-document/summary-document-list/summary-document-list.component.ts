import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnDestroy, OnInit } from '@angular/core';

import { AlertService } from './../../../../core/alert/alert.service';
import { DataService } from './../../../../core/data/data.service';
import { Document } from './../../../../core/model/document.model';
import { NgForm } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OrderBy } from './../../../../core/model/order-by.model';
import { Organization } from './../../../../core/model/organization.model';
import { Paging } from './../../../../core/model/paging.model';
import { SearchCriteria } from './../../../../core/model/search-criteria.model';
import { SearchCriteriaFilter } from './../../../../core/model/search-criteria-filter.model';
import { SearchResults } from './../../../../core/model/search-results.model';
import { Subscription } from 'rxjs/Subscription';
import { URLSearchParams } from '@angular/http';

@Component({
  selector: 'of-summary-document-list',
  templateUrl: './summary-document-list.component.html',
  styles: []
})
export class SummaryDocumentListComponent implements OnInit, OnDestroy {

  dataSubscription: Subscription;

  organization: Organization;
  searchResult: SearchResults<Document> = new SearchResults<Document>();

  thirdPartyByEmail: any = {};

  // Search Criteria
  searchCriteria: SearchCriteria = {
    filterText: null
  };
  filters: Array<SearchCriteriaFilter> = new Array<SearchCriteriaFilter>();
  orderBy: OrderBy = {
    name: 'createdTimestamp',
    ascending: false
  };
  paging: Paging = {
    page: 1,
    pageSize: 10
  };

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private modalService: NgbModal,
    private dataService: DataService,
    private alertService: AlertService) {
  }

  ngOnInit() {
    this.dataSubscription = this.route.parent.data.subscribe(data => {
      this.organization = data['organization'];
      this.search();
    });
  }

  ngOnDestroy() {
    this.dataSubscription.unsubscribe();
  }

  createNewDocument() {
    this.router.navigate(['./create'], { relativeTo: this.route });
  }

  editDocument(document: Document) {
    this.router.navigate(['./', document.id], { relativeTo: this.route });
  }

  search() {
    const criteria: SearchCriteria = {
      filterText: this.searchCriteria.filterText,
      filters: this.filters.map(f => {
        return new SearchCriteriaFilter(f.name, f.value, f.operator, f.type);
      }),
      orders: [this.orderBy],
      paging: this.paging
    };
    criteria.filters.push(new SearchCriteriaFilter('documentType', 'SUMMARY_DOCUMENTS', 'eq'));

    this.dataService.documents().search(this.organization, criteria).subscribe(
      (data) => {
        this.searchResult = data;
      }
    );
  }

  hasRequiredAction(item: any, requiredAction: string) {
    if (item.requiredActions && item.requiredActions.length > 0) {
      return item.requiredActions.indexOf(requiredAction) >= 0;
    } else {
      return false;
    }
  }

  /**
   * Sort
   */
  changeAscending() {
    this.orderBy.ascending = !this.orderBy.ascending;
    this.search();
  }

  /**
   * Pagination
   */
  changePageSize(pageSize: number) {
    this.paging.pageSize = pageSize;
    this.search();
  }

  nextPage() {
    this.paging.page++;
    if (this.paging.page > this.getTotalNumberOfPages()) {
      this.paging.page = this.getTotalNumberOfPages();
    } else {
      this.search();
    }
  }

  previousPage() {
    this.paging.page--;
    if (this.paging.page < 1) {
      this.paging.page = 1;
    } else {
      this.search();
    }
  }

  firstPage() {
    if (this.paging.page !== 1) {
      this.paging.page = 1;
      this.search();
    }
  }

  lastPage() {
    if (this.paging.page !== this.getTotalNumberOfPages()) {
      this.paging.page = +(this.searchResult.totalSize / this.paging.pageSize).toFixed(0);
      this.search();
    }
  }

  getTotalNumberOfPages() {
    return Math.floor(this.searchResult.totalSize / this.paging.pageSize) + 1;
  }

  /**
   * Filters
   */

  clearFilters() {
    this.filters = new Array<SearchCriteriaFilter>();
    this.search();
  }

  removeFilter(index: number) {
    this.filters.splice(index, 1);
    this.search();
  }

  addNoEnviadosSunatFilter() {
    const filter = new SearchCriteriaFilter('requiredActions', 'SEND_TO_THIRD_PARTY', 'in');
    filter.alias = 'No Enviados a Sunat';
    this.filters.push(filter);
    this.search();
  }

  addNoEnviadosClienteFilter() {
    const filter = new SearchCriteriaFilter('requiredActions', 'SEND_TO_CUSTOMER', 'in');
    filter.alias = 'No Enviados a Cliente';
    this.filters.push(filter);
    this.search();
  }

  addUltimaHoraFilter() {
    const date = new Date();
    date.setHours(date.getHours() - 1);
    const filter = new SearchCriteriaFilter('createdTimestamp', date, 'gt', 'DATETIME');
    filter.alias = 'Ultima Hora';
    this.filters.push(filter);
    this.search();
  }

  addUltimas24HorasFilter() {
    const date = new Date();
    date.setHours(date.getHours() - 24);
    const filter = new SearchCriteriaFilter('createdTimestamp', date, 'gt', 'DATETIME');
    filter.alias = 'Ultimas 24 Horas';
    this.filters.push(filter);
    this.search();
  }

  addUltimoMesFilter() {
    const date = new Date();
    date.setMonth(date.getMonth() - 1);
    const filter = new SearchCriteriaFilter('createdTimestamp', date, 'gt', 'DATETIME');
    filter.alias = 'Ultimo mes';
    this.filters.push(filter);
    this.search();
  }

  /**
   * Actions
   */
  downloadXml(document: Document) {
    document.downloadXml();
  }

  downloadCdr(document: Document) {
    this.dataService.organizationsSunat().downloadDocumentCdr(this.organization.organization, document.id);
  }

  downloadPdf(document: Document) {
    const queryParams: URLSearchParams = new URLSearchParams();
    queryParams.set('format', 'pdf');
    document.downloadReport(queryParams);
  }

  sendToCustomer(document: Document) {
    document.sendToCustomer().subscribe(result => {
      this.alertService.pop('success', 'Success', 'Success! Document sended to customer.');
    });
  }

  sendToThirdParty(invoice: Document) {
    invoice.sendToThirdParty().subscribe(result => {
      this.alertService.pop('success', 'Success', 'Success! Document sended to third party.');
    });
  }

  sendToCustomThridPartyByEmail(invoice: Document, content: any) {
    this.modalService.open(content).result.then(
      (form: NgForm) => {
        if (form.valid) {
          invoice.sendToThirdPartyByEmail({ email: form.value.thirdPartyByEmail.email }).subscribe(data => {
            this.alertService.pop('success', 'Success', 'Success! Document sended to third party.');
          });
        }
      }, (reason) => { });
  }

}

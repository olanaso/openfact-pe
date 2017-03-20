import { Model } from './model';
import { Observable } from 'rxjs/Observable';
import { RestangularService } from './../data/restangular.service';
import { URLSearchParams } from '@angular/http';

export class Organization extends Model {

    id: string;
    organization: string;

    internationalizationEnabled: boolean;
    defaultLocale: string;

    smtpServer: SmtpServerConfig;

    taskDelay: number;
    taskFirstTime: Date;

    attributes: any;


    assignedIdentificationId: string;
    additionalAccountId: string;
    supplierName: string;
    registrationName: string;
    postalAddress: PostalAddressRepresentation;


    constructor(restangular: RestangularService) {
        super(restangular);
    }

    build(): Organization {
        return new Organization(this.restangular);
    }

    getOrganizationKeys(queryParams?: URLSearchParams) {
        return this.restangular
            .all('keys')
            .get(queryParams)
            .map(response => response.json());
    }

    getComponent(componentId: string, queryParams?: URLSearchParams) {
        return this.restangular
            .one('components', componentId)
            .get(queryParams)
            .map(response => response.json());
    }

    getComponents(queryParams?: URLSearchParams) {
        return this.restangular
            .all('components')
            .get(queryParams)
            .map(response => response.json());
    }

    createComponent(component: any) {
        return this.restangular
            .all('components')
            .post(component);
    }

    updateComponent(componentId: string, component: any) {
        return this.restangular
            .one('components', componentId)
            .put(component);
    }

    removeComponent(componentId: string) {
        return this.restangular
            .one('components', componentId)
            .delete();
    }

    getEventsConfig(queryParams?: URLSearchParams) {
        return this.restangular
            .all('events/config')
            .get(queryParams)
            .map(response => response.json());
    }

    updateEventsConfig(config: any) {
        return this.restangular
            .all('events/config')
            .put(config);
    }

    clearAdminEvents() {
        return this.restangular
            .all('admin-events')
            .delete();
    }

    getAdminEvents(queryParams?: URLSearchParams) {
        return this.restangular
            .all('admin-events')
            .get(queryParams)
            .map(response => response.json());
    }

    getTaskProviders(queryParams?: URLSearchParams): Observable<any> {
        return this.restangular
            .all('job-reports')
            .all('providers')
            .get(queryParams)
            .map(response => response.json());
    }

    public getAllTasks(queryParams?: URLSearchParams): Observable<any[]> {
        return this.restangular
            .all('job-reports')
            .get(queryParams)
            .map(response => <any[]>response.json());
    }

}

export interface SmtpServerConfig {
    host: string;
    port: string;
    from?: string;
    ssl?: string;
    starttls?: string;
    auth?: string;
    user?: string;
    password?: string;
}

export interface PostalAddressRepresentation {
    streetName: string;
    citySubdivisionName: string;
    cityName: string;
    countrySubentity: string;
    district: string;
    countryIdentificationCode: string;
}

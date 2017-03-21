import { FileUploader } from 'ng2-file-upload';
import { Injectable } from '@angular/core';
import { KeycloakHttp } from './../keycloak.http';
import { Organization } from './../model/organization.model';
import { RestangularService } from './restangular.service';

export const basePath = 'sunat';

@Injectable()
export class RetentionService {

  constructor(private restangular: RestangularService) { }

  getFileUpload(organization: Organization): FileUploader {
    const restangular = this.restangular
      .one('organizations', organization.organization)
      .all(basePath)
      .all('ubl-extensions/retentions');

    const upload = new FileUploader({
      url: restangular.path,
      headers: [KeycloakHttp.getToken()]
    });
    return upload;
  }

}

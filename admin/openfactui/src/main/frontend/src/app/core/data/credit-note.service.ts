import { FileUploader } from 'ng2-file-upload';
import { Injectable } from '@angular/core';
import { KeycloakHttp } from './../keycloak.http';
import { Organization } from './../model/organization.model';

@Injectable()
export class CreditNoteService {

  constructor() { }

  getFileUpload(organization: Organization): FileUploader {
    const restangular = organization.restangular.all('credit-notes').all('upload');
    const upload = new FileUploader({
      url: restangular.path,
      headers: [KeycloakHttp.getToken()]
    });
    return upload;
  }

}

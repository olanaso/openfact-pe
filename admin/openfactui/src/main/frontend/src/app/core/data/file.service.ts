import { Headers, ResponseContentType } from '@angular/http';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Organization } from './../model/organization.model';
import { saveAs } from 'file-saver';

@Injectable()
export class FileService {

  constructor() { }

  download(organization: Organization, id: string, fileName?: string) {
    const restangular = organization.restangular.one('storage-files', id);
    return restangular.http
      .get(restangular.path, {
        headers: new Headers(),
        responseType: ResponseContentType.Blob
      })
      .map(response => {
        const file = {
          file: response.blob(),
          fileName: fileName || (this['fileName'] || 'file')
        };
        return file;
      }).subscribe(result => {
        saveAs(result.file, result.fileName);
      }, error => {
        Observable.throw(error);
      });
  }

}

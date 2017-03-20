import { RequestOptionsArgs, ResponseContentType } from '@angular/http';

import { Document } from './../model/document.model';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { Organization } from './../model/organization.model';
import { RestangularService } from './restangular.service';
import { saveAs } from 'file-saver';

export const genericIdName = 'id';
export const basePath = 'sunat';
export const extensionPath = 'ubl21-extensions';
export const genericBasePath = 'generic-types';

@Injectable()
export class OrganizationSunatService {

  constructor(private restangular: RestangularService) { }

  downloadDocumentCdr(organizationName: string, documentId: string) {
    const options: RequestOptionsArgs = {
      responseType: ResponseContentType.Blob
    };

    return this.restangular.one('admin/organizations', organizationName)
      .all(basePath)
      .one('documents', documentId)
      .all('cdr')
      .get(null, options)
      .map(response => {
        const file = {
          file: response.blob(),
          fileName: 'cdr.zip'
        };
        return file;
      }).subscribe(result => {
        saveAs(result.file, result.fileName);
      }, error => {
        Observable.throw(error);
      });
  }

  checkTicket(organizationName: string, documentId: string) {
    const options: RequestOptionsArgs = {
      responseType: ResponseContentType.Blob
    };

    return this.restangular.one('admin/organizations', organizationName)
      .all(basePath)
      .one('documents', documentId)
      .all('check-ticket')
      .get(null, options)
      .map(response => {
        const file = {
          file: response.blob(),
          fileName: 'ticket.zip'
        };
        return file;
      }).subscribe(result => {
        saveAs(result.file, result.fileName);
      }, error => {
        Observable.throw(error);
      });
  }

  createInvoice(organizationName: string, document: any): Observable<any> {
    return this.restangular.one('admin/organizations', organizationName)
      .all(basePath)
      .all('documents/invoices')
      .post(document)
      .map(response => {
        if (response.status === 201 || 204) {
          return undefined;
        }
        return response.json();
      });
  }

  createCreditnote(organizationName: string, invoice: any): Observable<any> {
    return this.restangular.one('admin/organizations', organizationName)
      .all(basePath)
      .all('documents/credit-notes')
      .post(invoice)
      .map(response => {
        if (response.status === 201 || 204) {
          return undefined;
        }
        return response.json();
      });
  }

  createDebitNotes(organizationName: string, invoice: any): Observable<any> {
    return this.restangular.one('admin/organizations', organizationName)
      .all(basePath)
      .all('documents/debit-notes')
      .post(invoice)
      .map(response => {
        if (response.status === 201 || 204) {
          return undefined;
        }
        return response.json();
      });
  }

  createPerception(organizationName: string, document: any): Observable<any> {
    return this.restangular.one('admin/organizations', organizationName)
      .all(basePath)
      .all('documents/perceptions')
      .post(document)
      .map(response => {
        if (response.status === 201 || 204) {
          return undefined;
        }
        return response.json();
      });
  }

  createRetention(organizationName: string, document: any): Observable<any> {
    return this.restangular.one('admin/organizations', organizationName)
      .all(basePath)
      .all('documents/retentions')
      .post(document)
      .map(response => {
        if (response.status === 201 || 204) {
          return undefined;
        }
        return response.json();
      });
  }

  createVoidedDocument(organizationName: string, document: any): Observable<any> {
    return this.restangular.one('admin/organizations', organizationName)
      .all(basePath)
      .all('documents/voided-documents')
      .post(document)
      .map(response => {
        if (response.status === 201 || 204) {
          return undefined;
        }
        return response.json();
      });
  }

  getAllTiposComprobantePago(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('tipos-comprobante-pago')
      .get()
      .map(response => response.json());
  }

  getAllTiposNotaCredito(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('tipos-nota-credito')
      .get()
      .map(response => response.json());
  }

  getAllTiposNotaDebito(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('tipos-nota-debito')
      .get()
      .map(response => response.json());
  }

  getAllTiposDocumentEntidad(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('tipos-documento-entidad')
      .get()
      .map(response => response.json());
  }

  getAllTiposAfectacionIGV(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('tipos-afectacion-igv')
      .get()
      .map(response => response.json());
  }

  getIgv(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('igv')
      .get()
      .map(response => response.json());
  }

  getTiposRegimenPercepcion(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('tipos-regimen-percepcion')
      .get()
      .map(response => response.json());
  }

  getDocumentosRelacionadosPercepcion(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('documentos-relacionados-percepcion')
      .get()
      .map(response => response.json());
  }

  getTiposRegimenRetencion(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('tipos-regimen-retencion')
      .get()
      .map(response => response.json());
  }

  getDocumentosRelacionadosRetencion(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('documentos-relacionados-retencion')
      .get()
      .map(response => response.json());
  }

  getMonedas(organizationName: string): Observable<any> {
    return this.restangular.one('organizations', organizationName)
      .all(basePath)
      .all(genericBasePath)
      .all('monedas')
      .get()
      .map(response => response.json());
  }

}

import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';

import { DataService } from './../data/data.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { findParam } from './find-param';

@Injectable()
export class TiposComprobantePagoResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getAllTiposComprobantePago(organizationId);
  }

}

@Injectable()
export class TiposNotaCreditoResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getAllTiposNotaCredito(organizationId);
  }

}

@Injectable()
export class TiposNotaDebitoResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getAllTiposNotaDebito(organizationId);
  }

}

@Injectable()
export class TiposDocumentoEntidadResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getAllTiposDocumentEntidad(organizationId);
  }

}

@Injectable()
export class TiposAfectacionIGVResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getAllTiposAfectacionIGV(organizationId);
  }

}

@Injectable()
export class IgvResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getIgv(organizationId);
  }

}

@Injectable()
export class TiposRegimenPercepcionResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getTiposRegimenPercepcion(organizationId);
  }

}

@Injectable()
export class DocumentosRelacionadosPercepcionResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getDocumentosRelacionadosPercepcion(organizationId);
  }

}

@Injectable()
export class TiposRegimenRetencionResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getTiposRegimenRetencion(organizationId);
  }

}

@Injectable()
export class DocumentosRelacionadosRetencionResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getDocumentosRelacionadosRetencion(organizationId);
  }

}

@Injectable()
export class MonedasResolverService implements Resolve<any> {

  constructor(private dataService: DataService) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> {
    const organizationId = findParam('organization', route);
    return this.dataService.organizationsSunat().getMonedas(organizationId);
  }

}

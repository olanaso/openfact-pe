import { ConnectionBackend, Headers, Http, Request, RequestOptions, RequestOptionsArgs, Response, XHRBackend } from '@angular/http';

import { Injectable } from '@angular/core';
import { KeycloakService } from './keycloak.service';
import { LoadingService } from './loading/loading.service';
import { Observable } from 'rxjs/Rx';

export function KeycloakHttpFactory(
    backend: XHRBackend,
    defaultOptions: RequestOptions,
    keycloakService: KeycloakService,
    loadingService: LoadingService
) {
    return new KeycloakHttp(backend, defaultOptions, keycloakService, loadingService);
}

export class KeycloakHttp extends Http {

    public static getToken() {
        return {
            name: 'Authorization',
            value: 'Bearer ' + KeycloakService.auth.authz.token
        };
    }

    constructor(
        _backend: ConnectionBackend,
        _defaultOptions: RequestOptions,
        private _keycloakService: KeycloakService,
        private _loadingService: LoadingService
    ) {
        super(_backend, _defaultOptions);
    }

    private setToken(options: RequestOptionsArgs) {
        if (options == null
            || KeycloakService.auth == null
            || KeycloakService.auth.authz == null
            || KeycloakService.auth.authz.token == null) {
            console.log('Need a token, but no token is available, not setting bearer token.');
            return;
        }
        if (KeycloakService.auth.authorization && KeycloakService.auth.authorization.rpt && options.url.indexOf('/authorize') === -1) {
            options.headers.set('Authorization', 'Bearer ' + KeycloakService.auth.authorization.rpt);
        } else {
            options.headers.set('Authorization', 'Bearer ' + KeycloakService.auth.authz.token);
        }
    }

    private setTokenOnExistedRequest(request: Request) {
        if (request == null ||
            KeycloakService.auth == null ||
            KeycloakService.auth.authz == null ||
            KeycloakService.auth.authz.token == null) {
            console.log('Need a token, but no token is available, not setting bearer token.');
            return;
        }
        if (KeycloakService.auth.authorization && KeycloakService.auth.authorization.rpt && request.url.indexOf('/authorize') === -1) {
            request.headers.set('Authorization', 'Bearer ' + KeycloakService.auth.authorization.rpt);
        } else {
            request.headers.set('Authorization', 'Bearer ' + KeycloakService.auth.authz.token);
        }
    }

    private configureRequest(f: Function, url: string | Request, options: RequestOptionsArgs, body?: any): Observable<Response> {
        const tokenPromise: Promise<string> = this._keycloakService.getToken();
        const tokenObservable: Observable<string> = Observable.fromPromise(tokenPromise);

        const tokenRequestUpdateObservable: Observable<any> = Observable.create((observer) => {
            if (url instanceof Request) {
                const request = <Request>url;
                if (!request.headers.get('Authorization')) {
                    this.setTokenOnExistedRequest(url);
                }
            }
            observer.next();
            observer.complete();
        });

        const tokenUpdateObservable: Observable<any> = Observable.create((observer) => {
            if (options == null) {
                const headers = new Headers();
                options = new RequestOptions({ headers: headers });
            }

            this.setToken(options);
            observer.next();
            observer.complete();
        });
        const requestObservable: Observable<Response> = Observable.create((observer) => {
            let result;
            if (body) {
                result = f.apply(this, [url, body, options]);
            } else {
                result = f.apply(this, [url, options]);
            }

            this._loadingService.incrementResourceRequests();
            result.subscribe(
                (response) => {
                    this._loadingService.reduceResourceRequests();
                    observer.next(response);
                    observer.complete();
                },
                (err) => {
                    this._loadingService.reduceResourceRequests();
                    observer.error(err);
                }
            );
        });

        return <Observable<Response>>Observable
            .merge(tokenObservable, tokenUpdateObservable, requestObservable, 1)
            .merge(tokenRequestUpdateObservable) // Insure no concurrency in the merged Observables
            .filter((response) => response instanceof Response);
    }



    /**
     * Performs any type of http request. First argument is required, and can either be a url or
     * a {@link Request} instance. If the first argument is a url, an optional {@link RequestOptions}
     * object can be provided as the 2nd argument. The options object will be merged with the values
     * of {@link BaseRequestOptions} before performing the request.
     */
    request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
        return this.configureRequest(super.request, url, options);
    }

    /**
     * Performs a request with `get` http method.
     */
    get(url: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.configureRequest(super.get, url, options);
    }

    /**
     * Performs a request with `post` http method.
     */
    post(url: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
        return this.configureRequest(super.post, url, options, body);
    }

    /**
     * Performs a request with `put` http method.
     */
    put(url: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
        return this.configureRequest(super.put, url, options, body);
    }

    /**
     * Performs a request with `delete` http method.
     */
    delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.configureRequest(super.delete, url, options);
    }

    /**
     * Performs a request with `patch` http method.
     */
    patch(url: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
        return this.configureRequest(super.patch, url, options, body);
    }

    /**
     * Performs a request with `head` http method.
     */
    head(url: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.configureRequest(super.head, url, options);
    }

    /**
     * Performs a request with `options` http method.
     */
    options(url: string, options?: RequestOptionsArgs): Observable<Response> {
        return this.configureRequest(super.options, url, options);
    }

}

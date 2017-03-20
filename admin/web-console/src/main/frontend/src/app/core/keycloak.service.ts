import { Injectable } from '@angular/core';

declare var Keycloak: any;
declare var KeycloakAuthorization: any;

@Injectable()
export class KeycloakService {

  static auth: any = {};

  static init(): Promise<any> {
    const keycloakAuth: any = new Keycloak('assets/keycloak.json');

    return new Promise((resolve, reject) => {
      keycloakAuth.init({ onLoad: 'login-required' })
        .success(() => {
          KeycloakService.auth.authz = keycloakAuth;
          KeycloakService.auth.authorization = new KeycloakAuthorization(keycloakAuth);
          resolve();
        })
        .error(() => {
          reject();
        });
    });
  }

  constructor() { }

  getToken(): Promise<string> {
    return new Promise<string>((resolve, reject) => {
      if (KeycloakService.auth.authz.token) {
        KeycloakService.auth.authz.updateToken(5)
          .success(() => {
            resolve(<string>KeycloakService.auth.authz.token);
          })
          .error(() => {
            reject('Failed to refresh token');
          });
      }
    });
  }

}

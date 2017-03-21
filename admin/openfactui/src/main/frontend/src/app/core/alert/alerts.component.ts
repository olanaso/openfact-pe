import { Alert, AlertLink } from './alert';
import { AlertService, IClearWrapper } from './alert.service';
import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';

import { AlertConfig } from './alert-config';

@Component({
  selector: 'of-alerts',
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.scss']
})
export class AlertsComponent implements OnInit, OnDestroy {

  private addAlertSubscriber: any;
  private clearAlertsSubscriber: any;
  private alertService: AlertService;

  @Input() alertConfig: AlertConfig;

  public alerts: Alert[] = [];

  constructor(alertService: AlertService, private ref: ChangeDetectorRef) {
    this.alertService = alertService;
  }

  ngOnInit() {
    this.registerSubscribers();
    if (this.alertConfig === null || typeof this.alertConfig === 'undefined') {
      this.alertConfig = new AlertConfig();
    }
  }

  // event handlers
  click(alert: Alert, isCloseButton?: boolean) {
    if (alert.showCloseButton && isCloseButton) {
      this.removeAlert(alert);
    }
  }

  childClick($event) {
    this.click($event.value.alert, $event.value.isCloseButton);
  }

  stopTimer(alert: Alert) {
    if (this.alertConfig.mouseoverTimerStop) {
      if (alert.timeoutId) {
        window.clearTimeout(alert.timeoutId);
        alert.timeoutId = null;
      }
    }
  }

  restartTimer(alert: Alert) {
    if (this.alertConfig.mouseoverTimerStop) {
      if (alert.timeoutId) {
        this.configureTimer(alert);
      }
    } else if (alert.timeoutId === null) {
      this.removeAlert(alert);
    }
  }

  // private functions
  private registerSubscribers() {
    this.addAlertSubscriber = this.alertService.addAlert.subscribe((alert: Alert) => {
      this.addAlert(alert);
    });

    this.clearAlertsSubscriber = this.alertService.clearAlerts.subscribe((clearWrapper: IClearWrapper) => {
      this.clearAlerts(clearWrapper);
    });
  }

  private addAlert(alert: Alert) {
    alert.alertConfig = this.alertConfig;

    if (alert.alertContainerId &&
      this.alertConfig.alertContainerId &&
      alert.alertContainerId !== this.alertConfig.alertContainerId) {
      return;
    }

    if (!alert.type) {
      alert.type = this.alertConfig.defaultType;
    }

    if (alert.showCloseButton === null || typeof alert.showCloseButton === 'undefined') {
      if (typeof this.alertConfig.showCloseButton === 'object') {
        alert.showCloseButton = this.alertConfig.showCloseButton[alert.type];
      } else if (typeof this.alertConfig.showCloseButton === 'boolean') {
        alert.showCloseButton = <boolean>this.alertConfig.showCloseButton;
      }
    }

    if (!alert.toast) {
      alert.toast = this.alertConfig.toast;
    }

    this.configureTimer(alert);

    this.alerts.push(alert);
    if (this.isLimitExceeded()) {
      this.alerts.shift();
    }
  }

  private configureTimer(alert: Alert) {
    let timeout = (typeof alert.timeout === 'number') ? alert.timeout : this.alertConfig.timeout;

    if (typeof timeout === 'object') {
      timeout = timeout[alert.type];
    }
    if (timeout > 0) {
      alert.timeoutId = window.setTimeout(() => {
        this.ref.markForCheck();
        this.removeAlert(alert);
      }, timeout);
    }
  }

  private isLimitExceeded() {
    return this.alertConfig.limit && this.alerts.length > this.alertConfig.limit;
  }

  private removeAlert(alert: Alert) {
    const index = this.alerts.indexOf(alert);
    if (index < 0) {
      return;
    }

    this.alerts.splice(index, 1);
    if (alert.timeoutId) {
      window.clearTimeout(alert.timeoutId);
      alert.timeoutId = null;
    }
    if (alert.onHideCallback) {
      alert.onHideCallback(alert);
    }
  }

  private removeAllAlerts() {
    for (let i = this.alerts.length - 1; i >= 0; i--) {
      this.removeAlert(this.alerts[i]);
    }
  }

  private clearAlerts(clearWrapper: IClearWrapper) {
    const alertId = clearWrapper.alertId;
    const alertContainerId = clearWrapper.alertContainerId;

    if (alertContainerId === null || typeof alertContainerId === 'undefined') {
      this.clearAlertsAction(alertId);
    } else if (alertContainerId === this.alertConfig.alertContainerId) {
      this.clearAlertsAction(alertId);
    }
  }

  private clearAlertsAction(alertId?: string) {
    if (alertId) {
      this.removeAlert(this.alerts.filter(t => t.alertId === alertId)[0]);
    } else {
      this.removeAllAlerts();
    }
  }

  ngOnDestroy() {
    if (this.addAlertSubscriber) { this.addAlertSubscriber.unsubscribe(); }
    if (this.clearAlertsSubscriber) { this.clearAlertsSubscriber.unsubscribe(); }
  }

}

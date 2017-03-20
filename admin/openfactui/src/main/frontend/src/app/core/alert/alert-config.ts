export class AlertConfig implements IAlertConfig {
    limit: number;
    alertContainerId: number;
    defaultType: string;
    showCloseButton: boolean | Object;
    timeout: number | Object;
    mouseoverTimerStop: boolean;

    toast: boolean | Object;

    constructor(configOverrides?: IAlertConfig) {
        configOverrides = configOverrides || {};
        this.limit = configOverrides.limit || 1;
        this.alertContainerId = configOverrides.alertContainerId != null ? configOverrides.alertContainerId : null;
        this.defaultType = configOverrides.defaultType || 'info';
        this.showCloseButton = configOverrides.showCloseButton != null ? configOverrides.showCloseButton : true;
        this.timeout = configOverrides.timeout != null ? configOverrides.timeout : 5000;
        this.mouseoverTimerStop = configOverrides.mouseoverTimerStop != null ? configOverrides.mouseoverTimerStop : false;
        this.toast = configOverrides.toast != null ? configOverrides.toast : false;
    }

}

export interface IAlertConfig {
    limit?: number;
    alertContainerId?: number;
    defaultType?: string;
    showCloseButton?: boolean | Object;
    timeout?: number | Object;
    mouseoverTimerStop?: boolean;

    toast?: boolean | Object;
}

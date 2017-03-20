import { AlertConfig } from './alert-config';

export interface Alert {
    alertId?: string;
    alertContainerId?: number;

    type: string;
    message: string;
    details?: string;
    links?: AlertLink[];

    toast?: boolean | Object;
    timeout?: number;
    timeoutId?: number;
    showCloseButton?: boolean;

    onShowCallback?: OnActionCallback;
    onHideCallback?: OnActionCallback;

    alertConfig?: AlertConfig;
}

export interface AlertLink {
    label: string;
    href?: string;
    onClickCallback?: OnActionCallback;
}

export type OnActionCallback = (toast: Alert) => void;

import {
  DocumentosRelacionadosPercepcionResolverService,
  DocumentosRelacionadosRetencionResolverService,
  IgvResolverService,
  MonedasResolverService,
  TiposAfectacionIGVResolverService,
  TiposComprobantePagoResolverService,
  TiposDocumentoEntidadResolverService,
  TiposNotaCreditoResolverService,
  TiposNotaDebitoResolverService,
  TiposRegimenPercepcionResolverService,
  TiposRegimenRetencionResolverService,
} from './../core/resolvers/generic-type-resolver.service';
import { RouterModule, Routes } from '@angular/router';

import { AdminEventsComponent } from './events/admin-events/admin-events.component';
import { CreditNoteCreateComponent } from './documents/credit-notes/credit-note-create/credit-note-create.component';
import { CreditNoteEditComponent } from './documents/credit-notes/credit-note-edit/credit-note-edit.component';
import { CreditNoteListComponent } from './documents/credit-notes/credit-note-list/credit-note-list.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DebitNoteCreateComponent } from './documents/debit-notes/debit-note-create/debit-note-create.component';
import { DebitNoteEditComponent } from './documents/debit-notes/debit-note-edit/debit-note-edit.component';
import { DebitNoteListComponent } from './documents/debit-notes/debit-note-list/debit-note-list.component';
import { DocumentAttachedDocumentsComponent } from './documents/document-attached-documents/document-attached-documents.component';
import { DocumentResolverService } from './../core/resolvers/document-resolver.service';
import { DocumentSendEventsComponent } from './documents/document-send-events/document-send-events.component';
import { EventsConfigResolverService } from './../core/resolvers/events-config-resolver.service';
import { EventsSettingsComponent } from './events/events-settings/events-settings.component';
import { InvoiceCreateComponent } from './documents/invoices/invoice-create/invoice-create.component';
import { InvoiceEditComponent } from './documents/invoices/invoice-edit/invoice-edit.component';
import { InvoiceListComponent } from './documents/invoices/invoice-list/invoice-list.component';
import { NgModule } from '@angular/core';
import { OrganizationComponent } from './organization.component';
import { OrganizationComponentResolverService } from './../core/resolvers/organization-component-resolver.service';
import { OrganizationConfigGuardService } from './shared/organization-config-guard.service';
import { OrganizationKeyResolverService } from './../core/resolvers/organization-key-resolver.service';
import { OrganizationResolverService } from './../core/resolvers/organization-resolver.service';
import { PerceptionCreateComponent } from './documents/perceptions/perception-create/perception-create.component';
import { PerceptionEditComponent } from './documents/perceptions/perception-edit/perception-edit.component';
import { PerceptionListComponent } from './documents/perceptions/perception-list/perception-list.component';
import { RetentionCreateComponent } from './documents/retentions/retention-create/retention-create.component';
import { RetentionEditComponent } from './documents/retentions/retention-edit/retention-edit.component';
import { RetentionListComponent } from './documents/retentions/retention-list/retention-list.component';
import { ServerInfoResolverService } from './../core/resolvers/server-info-resolver.service';
import { SettingsActiveKeyComponent } from './settings/settings-active-key/settings-active-key.component';
import {
  SettingsAdditionalInformationComponent,
} from './settings/settings-additional-information/settings-additional-information.component';
import { SettingsAllKeysComponent } from './settings/settings-all-keys/settings-all-keys.component';
import { SettingsGeneralInformationComponent } from './settings/settings-general-information/settings-general-information.component';
import { SettingsGenericKeystoreComponent } from './settings/settings-generic-keystore/settings-generic-keystore.component';
import { SettingsKeyProvidersComponent } from './settings/settings-key-providers/settings-key-providers.component';
import { SettingsSmtpComponent } from './settings/settings-smtp/settings-smtp.component';
import { SettingsSunatComponent } from './settings/settings-sunat/settings-sunat.component';
import { SettingsTasksComponent } from './settings/settings-tasks/settings-tasks.component';
import { SettingsThemeComponent } from './settings/settings-theme/settings-theme.component';
import { SummaryDocumentEditComponent } from './documents/summary-document/summary-document-edit/summary-document-edit.component';
import { SummaryDocumentListComponent } from './documents/summary-document/summary-document-list/summary-document-list.component';
import { VoidedDocumentCreateComponent } from './documents/voided-document/voided-document-create/voided-document-create.component';
import { VoidedDocumentEditComponent } from './documents/voided-document/voided-document-edit/voided-document-edit.component';
import { VoidedDocumentListComponent } from './documents/voided-document/voided-document-list/voided-document-list.component';

const routes: Routes = [
  {
    path: '',
    component: OrganizationComponent,
    resolve: {
      organization: OrganizationResolverService
    },
    children: [
      {
        path: '',
        component: DashboardComponent
      },
      {
        path: 'settings/general-information',
        component: SettingsGeneralInformationComponent,
        resolve: {
          organization: OrganizationResolverService
        }
      },
      {
        path: 'settings/additional-information',
        component: SettingsAdditionalInformationComponent,
        resolve: {
          organization: OrganizationResolverService
        }
      },
      {
        path: 'settings/smtp-settings',
        component: SettingsSmtpComponent,
        resolve: {
          organization: OrganizationResolverService
        }
      },
      {
        path: 'settings/theme-settings',
        component: SettingsThemeComponent,
        resolve: {
          organization: OrganizationResolverService,
          serverInfo: ServerInfoResolverService
        }
      },
      {
        path: 'settings/tasks-settings',
        component: SettingsTasksComponent,
        resolve: {
          organization: OrganizationResolverService
        }
      },
      {
        path: 'settings/keys',
        component: SettingsActiveKeyComponent,
        resolve: {
          organization: OrganizationResolverService,
          keys: OrganizationKeyResolverService
        }
      },
      {
        path: 'settings/keys/list',
        component: SettingsAllKeysComponent,
        resolve: {
          organization: OrganizationResolverService,
          keys: OrganizationKeyResolverService
        }
      },
      {
        path: 'settings/keys/providers',
        component: SettingsKeyProvidersComponent,
        resolve: {
          organization: OrganizationResolverService,
          serverInfo: ServerInfoResolverService
        }
      },
      {
        path: 'settings/keys/providers/:provider',
        component: SettingsGenericKeystoreComponent,
        resolve: {
          organization: OrganizationResolverService,
          serverInfo: ServerInfoResolverService
        }
      },
      {
        path: 'settings/keys/providers/:provider/:component',
        component: SettingsGenericKeystoreComponent,
        resolve: {
          organization: OrganizationResolverService,
          serverInfo: ServerInfoResolverService,
          instance: OrganizationComponentResolverService
        }
      },
      {
        path: 'settings/sunat',
        component: SettingsSunatComponent,
        resolve: {
          organization: OrganizationResolverService
        }
      },
      {
        path: 'events',
        component: AdminEventsComponent
      },
      {
        path: 'events-settings',
        component: EventsSettingsComponent,
        resolve: {
          serverInfo: ServerInfoResolverService,
          eventsConfig: EventsConfigResolverService
        }
      },
      {
        path: 'documents/:document/send-events',
        component: DocumentSendEventsComponent,
        resolve: {
          document: DocumentResolverService
        }
      },
      {
        path: 'documents/:document/attached-documents',
        component: DocumentAttachedDocumentsComponent,
        resolve: {
          document: DocumentResolverService
        }
      },
      {
        path: 'invoices',
        component: InvoiceListComponent
      },
      {
        path: 'invoices/create',
        component: InvoiceCreateComponent,
        resolve: {
          tiposComprobantePago: TiposComprobantePagoResolverService,
          tiposDocumentEntidad: TiposDocumentoEntidadResolverService,
          tiposDeAfectacionIgv: TiposAfectacionIGVResolverService,
          igv: IgvResolverService
        },
      },
      {
        path: 'invoices/:document',
        component: InvoiceEditComponent,
        resolve: {
          document: DocumentResolverService
        }
      },
      {
        path: 'credit-notes',
        component: CreditNoteListComponent
      },
      {
        path: 'credit-notes/create',
        component: CreditNoteCreateComponent,
        resolve: {
          tiposNotaCredito: TiposNotaCreditoResolverService,
          tiposDocumentEntidad: TiposDocumentoEntidadResolverService,
          tiposDeAfectacionIgv: TiposAfectacionIGVResolverService,
          igv: IgvResolverService
        },
      },
      {
        path: 'credit-notes/:document',
        component: CreditNoteEditComponent,
        resolve: {
          document: DocumentResolverService
        }
      },
      {
        path: 'debit-notes',
        component: DebitNoteListComponent
      },
      {
        path: 'debit-notes/create',
        component: DebitNoteCreateComponent,
        resolve: {
          tiposNotaDebito: TiposNotaDebitoResolverService,
          tiposDocumentEntidad: TiposDocumentoEntidadResolverService,
          tiposDeAfectacionIgv: TiposAfectacionIGVResolverService,
          igv: IgvResolverService
        },

      },
      {
        path: 'debit-notes/:document',
        component: DebitNoteEditComponent,
        resolve: {
          document: DocumentResolverService
        }
      },
      {
        path: 'perceptions',
        component: PerceptionListComponent
      },
      {
        path: 'perceptions/create',
        component: PerceptionCreateComponent,
        resolve: {
          tiposRegimenPercepcion: TiposRegimenPercepcionResolverService,
          documentosRelacionadosPercepcion: DocumentosRelacionadosPercepcionResolverService,
          tiposDocumentEntidad: TiposDocumentoEntidadResolverService,
          monedas: MonedasResolverService
        },

      },
      {
        path: 'perceptions/:document',
        component: PerceptionEditComponent,
        resolve: {
          document: DocumentResolverService
        }
      },
      {
        path: 'retentions',
        component: RetentionListComponent
      },
      {
        path: 'retentions/create',
        component: RetentionCreateComponent,
        resolve: {
          tiposRegimenRetencion: TiposRegimenRetencionResolverService,
          documentosRelacionadosRetencion: DocumentosRelacionadosRetencionResolverService,
          tiposDocumentEntidad: TiposDocumentoEntidadResolverService,
          monedas: MonedasResolverService
        }
      },
      {
        path: 'retentions/:document',
        component: RetentionEditComponent,
        resolve: {
          document: DocumentResolverService
        }
      },
      {
        path: 'voided-documents',
        component: VoidedDocumentListComponent
      },
      {
        path: 'voided-documents/create',
        component: VoidedDocumentCreateComponent,

        resolve: {
          documentosRelacionadosVoid: DocumentosRelacionadosPercepcionResolverService,
        }
      },
      {
        path: 'voided-documents/:document',
        component: VoidedDocumentEditComponent,
        resolve: {
          document: DocumentResolverService
        }
      },
      {
        path: 'summary-documents',
        component: SummaryDocumentListComponent
      },
      {
        path: 'summary-documents/:document',
        component: SummaryDocumentEditComponent,
        resolve: {
          document: DocumentResolverService
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: []
})
export class OrganizationRoutingModule { }

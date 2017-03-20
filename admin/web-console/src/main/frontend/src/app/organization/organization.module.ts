import { AdminEventsComponent } from './events/admin-events/admin-events.component';
import { ComponentConfigComponent } from './settings/component-config/component-config.component';
import { CreditNoteCreateComponent } from './documents/credit-notes/credit-note-create/credit-note-create.component';
import { CreditNoteEditComponent } from './documents/credit-notes/credit-note-edit/credit-note-edit.component';
import { CreditNoteListComponent } from './documents/credit-notes/credit-note-list/credit-note-list.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DebitNoteCreateComponent } from './documents/debit-notes/debit-note-create/debit-note-create.component';
import { DebitNoteEditComponent } from './documents/debit-notes/debit-note-edit/debit-note-edit.component';
import { DebitNoteListComponent } from './documents/debit-notes/debit-note-list/debit-note-list.component';
import { DocumentActionsComponent } from './documents/document-actions/document-actions.component';
import { DocumentAttachedDocumentsComponent } from './documents/document-attached-documents/document-attached-documents.component';
import { DocumentEditHeaderComponent } from './documents/document-edit-header/document-edit-header.component';
import { DocumentEditRightSidebarComponent } from './documents/document-edit-right-sidebar/document-edit-right-sidebar.component';
import { DocumentSendEventsComponent } from './documents/document-send-events/document-send-events.component';
import { EventsSettingsComponent } from './events/events-settings/events-settings.component';
import { InvoiceCreateComponent } from './documents/invoices/invoice-create/invoice-create.component';
import { InvoiceEditComponent } from './documents/invoices/invoice-edit/invoice-edit.component';
import { InvoiceListComponent } from './documents/invoices/invoice-list/invoice-list.component';
import { NgModule } from '@angular/core';
import { OrganizationComponent } from './organization.component';
import { OrganizationConfigGuardService } from './shared/organization-config-guard.service';
import { OrganizationHeaderComponent } from './organization-header/organization-header.component';
import { OrganizationRoutingModule } from './organization-routing.module';
import { OrganizationSidebarComponent } from './organization-sidebar/organization-sidebar.component';
import { PerceptionCreateComponent } from './documents/perceptions/perception-create/perception-create.component';
import { PerceptionEditComponent } from './documents/perceptions/perception-edit/perception-edit.component';
import { PerceptionListComponent } from './documents/perceptions/perception-list/perception-list.component';
import { RetentionCreateComponent } from './documents/retentions/retention-create/retention-create.component';
import { RetentionEditComponent } from './documents/retentions/retention-edit/retention-edit.component';
import { RetentionListComponent } from './documents/retentions/retention-list/retention-list.component';
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
import { SettingsTabsetComponent } from './settings/settings-tabset/settings-tabset.component';
import { SettingsTasksComponent } from './settings/settings-tasks/settings-tasks.component';
import { SettingsThemeComponent } from './settings/settings-theme/settings-theme.component';
import { SharedModule } from './../shared/shared.module';
import { SummaryDocumentEditComponent } from './documents/summary-document/summary-document-edit/summary-document-edit.component';
import { SummaryDocumentListComponent } from './documents/summary-document/summary-document-list/summary-document-list.component';
import { VoidedDocumentCreateComponent } from './documents/voided-document/voided-document-create/voided-document-create.component';
import { VoidedDocumentEditComponent } from './documents/voided-document/voided-document-edit/voided-document-edit.component';
import { VoidedDocumentListComponent } from './documents/voided-document/voided-document-list/voided-document-list.component';

@NgModule({
  imports: [
    SharedModule,
    OrganizationRoutingModule
  ],
  declarations: [
    OrganizationComponent,
    OrganizationHeaderComponent,
    OrganizationSidebarComponent,
    SettingsGeneralInformationComponent,
    SettingsAdditionalInformationComponent,
    SettingsSmtpComponent,
    SettingsThemeComponent,
    SettingsTasksComponent,
    DashboardComponent,
    SettingsTabsetComponent,
    SettingsSmtpComponent,
    SettingsActiveKeyComponent,
    SettingsAllKeysComponent,
    SettingsKeyProvidersComponent,
    SettingsGenericKeystoreComponent,
    ComponentConfigComponent,
    InvoiceListComponent,
    CreditNoteListComponent,
    DebitNoteListComponent,
    InvoiceCreateComponent,
    CreditNoteCreateComponent,
    DebitNoteCreateComponent,
    InvoiceEditComponent,
    DocumentSendEventsComponent,
    DocumentAttachedDocumentsComponent,
    CreditNoteEditComponent,
    DebitNoteEditComponent,
    PerceptionCreateComponent,
    PerceptionEditComponent,
    PerceptionListComponent,
    RetentionCreateComponent,
    RetentionEditComponent,
    RetentionListComponent,
    VoidedDocumentCreateComponent,
    VoidedDocumentEditComponent,
    VoidedDocumentListComponent,
    EventsSettingsComponent,
    AdminEventsComponent,
    SettingsSunatComponent,
    DocumentEditHeaderComponent,
    DocumentEditRightSidebarComponent,
    DocumentActionsComponent,
    SummaryDocumentListComponent,
    SummaryDocumentEditComponent
  ],
  providers: [
    OrganizationConfigGuardService
  ]
})
export class OrganizationModule { }

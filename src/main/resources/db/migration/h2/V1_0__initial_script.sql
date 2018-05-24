
    create table ADMIN_EVENT_ENTITY (
       ID varchar(36) not null,
        IP_ADDRESS varchar(255),
        AUTH_ORGANIZATION_ID varchar(255),
        AUTH_USER_ID varchar(255),
        ERROR varchar(255),
        OPERATION_TYPE varchar(255),
        ORGANIZATION_ID varchar(255),
        REPRESENTATION varchar(25500),
        RESOURCE_PATH varchar(255),
        RESOURCE_TYPE varchar(64),
        ADMIN_EVENT_TIME bigint,
        primary key (ID)
    );

    create table ATTACHED_DOCUMENT (
       DOCUMENT_ORIGIN_ID varchar(255) not null,
        DOCUMENT_DESTINY_ID varchar(255) not null,
        primary key (DOCUMENT_DESTINY_ID, DOCUMENT_ORIGIN_ID)
    );

    create table COMPONENT (
       ID varchar(36) not null,
        NAME varchar(255),
        PARENT_ID varchar(255),
        PROVIDER_ID varchar(255),
        PROVIDER_TYPE varchar(255),
        SUB_TYPE varchar(255),
        ORGANIZATION_ID varchar(36) not null,
        primary key (ID)
    );

    create table COMPONENT_CONFIG (
       ID varchar(36) not null,
        NAME varchar(255),
        VALUE varchar(4000),
        COMPONENT_ID varchar(36),
        primary key (ID)
    );

    create table DOCUMENT (
       ID varchar(255) not null,
        CREATED_TIMESTAMP timestamp,
        CUSTOMER_ASSIGNED_ACCOUNT_ID varchar(255),
        CUSTOMER_ELECTRONIC_MAIL varchar(255),
        CUSTOMER_REGISTRATION_NAME varchar(255),
        CUSTOMER_SEND_EVENT_FAILURES integer,
        DOCUMENT_CURRENCY_CODE varchar(255),
        DOCUMENT_ID varchar(255) not null,
        DOCUMENT_TYPE varchar(255) not null,
        ENABLED integer,
        ORGANIZATION_ID varchar(255) not null,
        THIRD_PARTY_SEND_EVENT_FAILURES integer,
        XML_FILE_ID varchar(255),
        primary key (ID)
    );

    create table DOCUMENT_ATTRIBUTE (
       ID varchar(36) not null,
        NAME varchar(255),
        VALUE varchar(255),
        DOCUMENT_ID varchar(255),
        primary key (ID)
    );

    create table DOCUMENT_LINE (
       ID varchar(255) not null,
        DOCUMENT_ID varchar(255),
        primary key (ID)
    );

    create table DOCUMENT_LINE_ATTRIBUTE (
       NAME varchar(255) not null,
        VALUE varchar(600),
        DOCUMENT_LINE_ID varchar(255) not null,
        primary key (DOCUMENT_LINE_ID, NAME)
    );

    create table DOCUMENT_REQUIRED_ACTION (
       REQUIRED_ACTION varchar(255) not null,
        DOCUMENT_ID varchar(255) not null,
        primary key (REQUIRED_ACTION, DOCUMENT_ID)
    );

    create table EVENT_ENTITY (
       ID varchar(36) not null,
        DETAILS_JSON varchar(2550),
        ERROR varchar(255),
        IP_ADDRESS varchar(255),
        ORGANIZATION_ID varchar(255),
        EVENT_TIME bigint,
        TYPE varchar(255),
        USER_ID varchar(255),
        primary key (ID)
    );

    create table JOB_REPORT (
       ID varchar(255) not null,
        DURATION bigint,
        END_TIME bigint,
        ERROR_COUNT bigint,
        JOB_NAME varchar(255),
        ORGANIZATION_ID varchar(255) not null,
        READ_COUNT bigint,
        START_TIME bigint,
        WRITE_COUNT bigint,
        primary key (ID)
    );

    create table MIGRATION_MODEL (
       ID varchar(36) not null,
        VERSION varchar(36),
        primary key (ID)
    );

    create table ORGANIZATION (
       ID varchar(36) not null,
        ADDITIONAL_ACCOUNT_ID varchar(255),
        ADMIN_EVENTS_DETAILS_ENABLED integer,
        ADMIN_EVENTS_ENABLED integer,
        ASSIGNED_IDENTIFICATION_ID varchar(255),
        CITY_NAME varchar(255),
        CITY_SUBDIVISION_NAME varchar(255),
        COUNTRY_IDENTIFICATION_CODE varchar(255),
        COUNTRY_SUBENTITY varchar(255),
        CREATED_TIMESTAMP timestamp,
        DEFAULT_CURRENCY varchar(255),
        DEFAULT_LOCALE varchar(255),
        DESCRIPTION varchar(255),
        DISTRICT varchar(255),
        EMAIL_THEME varchar(255),
        ENABLED integer,
        EVENTS_ENABLED integer,
        EVENTS_EXPIRATION bigint,
        INTERNATIONALIZATION_ENABLED integer,
        LOGO_FILE_ID varchar(255),
        NAME varchar(255),
        POSTAL_ADRESS_ID varchar(255),
        REGISTRATION_NAME varchar(255),
        REPORT_THEME varchar(255),
        STREET_NAME varchar(255),
        SUPPLIER_NAME varchar(255),
        TASK_DELAY bigint,
        TASK_FIRST_TIME timestamp,
        TASK_ENABLED boolean,
        primary key (ID)
    );

    create table ORGANIZATION_ATTRIBUTE (
       NAME varchar(255) not null,
        VALUE varchar(255),
        ORGANIZATION_ID varchar(36) not null,
        primary key (NAME, ORGANIZATION_ID)
    );

    create table ORGANIZATION_ENABLED_EVENT_TYPES (
       ORGANIZATION_ID varchar(36) not null,
        VALUE varchar(255)
    );

    create table ORGANIZATION_EVENTS_LISTENERS (
       ORGANIZATION_ID varchar(36) not null,
        VALUE varchar(255)
    );

    create table ORGANIZATION_FILE (
       ID varchar(255) not null,
        FILE blob,
        FILE_NAME varchar(255),
        ORGANIZATION_ID varchar(36) not null,
        primary key (ID)
    );

    create table ORGANIZATION_SMTP_CONFIG (
       ORGANIZATION_ID varchar(36) not null,
        VALUE varchar(255),
        NAME varchar(255) not null,
        primary key (ORGANIZATION_ID, NAME)
    );

    create table ORGANIZATION_SUPPORTED_CURRENCIES (
       ORGANIZATION_ID varchar(36) not null,
        VALUE varchar(255)
    );

    create table ORGANIZATION_SUPPORTED_LOCALES (
       ORGANIZATION_ID varchar(36) not null,
        VALUE varchar(255)
    );

    create table SEND_EVENT (
       ID varchar(255) not null,
        CREATED_TIMESTAMP timestamp not null,
        DESCRIPTION varchar(400),
        DESTINY varchar(255) not null,
        STATUS varchar(255) not null,
        DOCUMENT_ID varchar(255) not null,
        primary key (ID)
    );

    create table SEND_EVENT_ATTACH_FILE (
       FILE_ID varchar(255) not null,
        SEND_EVENT_ID varchar(255) not null,
        primary key (FILE_ID, SEND_EVENT_ID)
    );

    create table SEND_EVENT_ATTRIBUTE (
       NAME varchar(255) not null,
        VALUE varchar(255),
        SEND_EVENT_ID varchar(255) not null,
        primary key (NAME, SEND_EVENT_ID)
    );

    create table SERIE_NUMERO (
       ID varchar(255) not null,
        DOCUMENT_TYPE varchar(255) not null,
        FIRST_LETTER varchar(255) not null,
        NUMERO integer not null,
        ORGANIZATION_ID varchar(255) not null,
        SERIE integer not null,
        primary key (ID)
    );

    alter table DOCUMENT
       add constraint UKpc1494kqudeufknje0ktnh0bi unique (ORGANIZATION_ID, DOCUMENT_TYPE, DOCUMENT_ID);

    alter table ORGANIZATION
       add constraint UK_3qxux5ykhins8i7d6074s5739 unique (NAME);

    alter table SERIE_NUMERO
       add constraint UKg8idf80osolqgg1r4d24edgi unique (DOCUMENT_TYPE, ORGANIZATION_ID, FIRST_LETTER);

    alter table ATTACHED_DOCUMENT
       add constraint FKmh71b5n5r3f0o9r2cujkhx1gs
       foreign key (DOCUMENT_ORIGIN_ID)
       references DOCUMENT;

    alter table ATTACHED_DOCUMENT
       add constraint FKhhsatpa61j98lodn32bv1t8yd
       foreign key (DOCUMENT_DESTINY_ID)
       references DOCUMENT;

    alter table COMPONENT
       add constraint FK3t48ej2mfxfg6kybl5e14935g
       foreign key (ORGANIZATION_ID)
       references ORGANIZATION;

    alter table COMPONENT_CONFIG
       add constraint FKkwy262tty5mdbhbwtlcwe1k0s
       foreign key (COMPONENT_ID)
       references COMPONENT;

    alter table DOCUMENT_ATTRIBUTE
       add constraint FKdp0ry6rnj3mf9vmxx32y8xflx
       foreign key (DOCUMENT_ID)
       references DOCUMENT;

    alter table DOCUMENT_LINE
       add constraint FKjc18itkgwnq151hdpenoeuqqc
       foreign key (DOCUMENT_ID)
       references DOCUMENT;

    alter table DOCUMENT_LINE_ATTRIBUTE
       add constraint FK4wdanrfpp4j5fylcabn2n89sd
       foreign key (DOCUMENT_LINE_ID)
       references DOCUMENT_LINE;

    alter table DOCUMENT_REQUIRED_ACTION
       add constraint FK88m0axmtnj6191bjpucow7u9t
       foreign key (DOCUMENT_ID)
       references DOCUMENT;

    alter table ORGANIZATION_ATTRIBUTE
       add constraint FKgd1yrfh8eneq62k7qspk7is1b
       foreign key (ORGANIZATION_ID)
       references ORGANIZATION;

    alter table ORGANIZATION_ENABLED_EVENT_TYPES
       add constraint FKoy4ev3uqqs8kxl309t24pv1nc
       foreign key (ORGANIZATION_ID)
       references ORGANIZATION;

    alter table ORGANIZATION_EVENTS_LISTENERS
       add constraint FKgt81tuti16jredd3jslnjg251
       foreign key (ORGANIZATION_ID)
       references ORGANIZATION;

    alter table ORGANIZATION_FILE
       add constraint FK2lwbox9r3c06c8iqn921qpj6y
       foreign key (ORGANIZATION_ID)
       references ORGANIZATION;

    alter table ORGANIZATION_SMTP_CONFIG
       add constraint FKrtba861aw6v8mpmhte184m5kg
       foreign key (ORGANIZATION_ID)
       references ORGANIZATION;

    alter table ORGANIZATION_SUPPORTED_CURRENCIES
       add constraint FKi8kcc84ua00henun2ferbfa19
       foreign key (ORGANIZATION_ID)
       references ORGANIZATION;

    alter table ORGANIZATION_SUPPORTED_LOCALES
       add constraint FKe9l4dt38bp58yikrlb26pi9eq
       foreign key (ORGANIZATION_ID)
       references ORGANIZATION;

    alter table SEND_EVENT
       add constraint FKdmu69aal6rd5aiug2lfb8nprq
       foreign key (DOCUMENT_ID)
       references DOCUMENT;

    alter table SEND_EVENT_ATTACH_FILE
       add constraint FKiy6nnqhbysg0uxrgrpeu9ewwh
       foreign key (SEND_EVENT_ID)
       references SEND_EVENT;

    alter table SEND_EVENT_ATTRIBUTE
       add constraint FKt4al34l6dolrbh57td8jpddh5
       foreign key (SEND_EVENT_ID)
       references SEND_EVENT;

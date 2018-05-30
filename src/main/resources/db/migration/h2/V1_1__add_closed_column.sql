-- ADD FOREIGN KEY FROM DOCUMENT TO ORGANIZATION
alter table DOCUMENT
  add constraint Fk_DOCUMENT_ORGANIZATION
  foreign key (ORGANIZATION_ID)
  references ORGANIZATION;

-- ADD CLOSED COLUMN TO DOCUMENT
alter table DOCUMENT
  add column CLOSED integer;

update DOCUMENT
  set CLOSED=1;

alter table DOCUMENT
  alter column CLOSED set not null;
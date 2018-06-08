-- ADD ISSUE_DATE COLUMN TO DOCUMENT
alter table DOCUMENT
  add column ISSUE_DATE timestamp;

alter table DOCUMENT
  alter column ISSUE_DATE set not null;
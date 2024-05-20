SET client_encoding = 'UTF8';
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE organizations(
    id                      UUID NOT NULL DEFAULT uuid_generate_v4(),
    name                    TEXT NOT NULL,
    address                 TEXT NOT NULL,
    email                   TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE donations(
    id                      UUID NOT NULL DEFAULT uuid_generate_v4(),
    organization_id         UUID NOT NULL,
    amount                  DECIMAL NOT NULL,
    is_donation_notified    BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (organization_id) REFERENCES organizations(id)
);

CREATE TABLE config(
    id                      UUID NOT NULL DEFAULT uuid_generate_v4(),
    name                    TEXT NOT NULL,
    subject                 TEXT NOT NULL,
    template                TEXT NOT NULL,
    PRIMARY KEY (id)
);
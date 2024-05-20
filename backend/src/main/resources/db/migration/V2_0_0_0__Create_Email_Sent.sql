CREATE TABLE email_sent(
    id                      UUID NOT NULL DEFAULT uuid_generate_v4(),
    organization_id         UUID NOT NULL,
    organization_email      TEXT NOT NULL,
    organization_name       TEXT NOT NULL,
    subject                 TEXT NOT NULL,
    content                 TEXT NOT NULL,
    send_date               TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
package com.temelio.nonprofit.email.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;


@Getter
public class ApplicationProperties {

    @Value("${database.hostname}")
    private String databaseHostname;

    @Value("${database.databasename:temelio}")
    private String databaseName;

    @Value("${database.username}")
    private String databaseUsername;

    @Value("${database.password}")
    private String databasePassword;

    @Value("${sendgrid.host}")
    private String sendGridHost;

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;
}

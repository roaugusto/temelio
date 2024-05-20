package com.temelio.nonprofit.email.config;

import com.temelio.nonprofit.email.service.impl.LocalSendGridEmailService;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

    @Primary
    @Bean(name = "dataSource")
    public DataSource hikariDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://%s/%s".formatted(applicationProperties().getDatabaseHostname(),
                applicationProperties().getDatabaseName()));
        ds.setUsername(applicationProperties().getDatabaseUsername());
        ds.setPassword(applicationProperties().getDatabasePassword());
        ds.setMaximumPoolSize(3);
        ds.setAutoCommit(true);
        ds.addDataSourceProperty("gssEncMode", "disable");
        return ds;
    }

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public LocalSendGridEmailService sendEmail() {
        return new LocalSendGridEmailService(applicationProperties(), httpClient());
    }

}

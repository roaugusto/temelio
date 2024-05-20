package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.config.ApplicationProperties;
import com.temelio.nonprofit.email.dto.ConfigDTO;
import com.temelio.nonprofit.email.model.DonationEntity;
import com.temelio.nonprofit.email.model.OrganizationEntity;
import com.temelio.nonprofit.email.service.impl.LocalSendGridEmailService;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LocalSendGridEmailServiceTest {

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private CloseableHttpResponse httpResponse;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpEntity httpEntity;


    @InjectMocks
    private LocalSendGridEmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSendEmailSuccessful() throws IOException {
        OrganizationEntity organization = new OrganizationEntity(UUID.randomUUID(), "Organization 1", "Address 1", "Email 1", null);
        DonationEntity donation = new DonationEntity(UUID.randomUUID(), 100.0, false, organization);

        when(applicationProperties.getSendGridHost()).thenReturn("https://example.com");
        when(applicationProperties.getSendGridApiKey()).thenReturn("api_key");
        when(applicationProperties.getSendGridHost()).thenReturn("https://example.com");
        when(applicationProperties.getSendGridApiKey()).thenReturn("api_key");
        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(202);
        when(httpResponse.getEntity()).thenReturn(httpEntity);

        ConfigDTO config = new ConfigDTO();
        config.setId(UUID.randomUUID());
        config.setName("Config 1");
        config.setSubject("Subject 1");
        config.setTemplate("Template 1");

        boolean result = emailService.sendEmail(donation, config.getSubject(), "");

        assertTrue(result);
    }

    @Test
    void testSendEmailFailed() throws IOException {
        OrganizationEntity organization = new OrganizationEntity(UUID.randomUUID(), "Organization 1", "Address 1", "Email 1", null);
        DonationEntity donation = new DonationEntity(UUID.randomUUID(), 100.0, false, organization);

        when(applicationProperties.getSendGridHost()).thenReturn("https://example.com");
        when(applicationProperties.getSendGridApiKey()).thenReturn("api_key");
        when(applicationProperties.getSendGridHost()).thenReturn("https://example.com");
        when(applicationProperties.getSendGridApiKey()).thenReturn("api_key");
        when(httpClient.execute(any())).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(httpResponse.getEntity()).thenReturn(httpEntity);

        ConfigDTO config = new ConfigDTO();
        config.setId(UUID.randomUUID());
        config.setName("Config 1");
        config.setSubject("Subject 1");
        config.setTemplate("Template 1");

        boolean result = emailService.sendEmail(donation, config.getSubject(), "");

        assertFalse(result);
    }
}

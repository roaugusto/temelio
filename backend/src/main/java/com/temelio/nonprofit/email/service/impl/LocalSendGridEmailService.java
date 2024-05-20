package com.temelio.nonprofit.email.service.impl;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.temelio.nonprofit.email.config.ApplicationProperties;
import com.temelio.nonprofit.email.dto.ConfigDTO;
import com.temelio.nonprofit.email.exception.BadRequestException;
import com.temelio.nonprofit.email.model.DonationEntity;
import com.temelio.nonprofit.email.service.EmailSender;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class LocalSendGridEmailService implements EmailSender {

    private final ApplicationProperties applicationProperties;
    private final CloseableHttpClient httpClient;

    public LocalSendGridEmailService(ApplicationProperties applicationProperties, CloseableHttpClient httpClient) {
        this.applicationProperties = applicationProperties;
        this.httpClient = httpClient;
    }

    @Override
    public boolean sendEmail(DonationEntity donation, String subject, String emailBody) {
        Email from = new Email(donation.getOrganization().getEmail());
        Email to = new Email(donation.getOrganization().getEmail());
        Content content = new Content("text/html", emailBody);
        Mail mail = new Mail(from, subject, to, content);

        try {
            HttpPost httpPost = new HttpPost(applicationProperties.getSendGridHost() + "/v3/mail/send");
            httpPost.setHeader("Authorization", "Bearer " + applicationProperties.getSendGridApiKey());
            httpPost.setHeader("Content-Type", "application/json");

            String jsonBody = mail.build();

            httpPost.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (response.getStatusLine().getStatusCode() == 202) {
                EntityUtils.consume(responseEntity);
                return true;
            } else {
                EntityUtils.consume(responseEntity);
                return false;
            }
        } catch (IOException ex) {
            throw new BadRequestException(String.format("Error sending e-mail: %s", ex.getMessage()));
        }
    }
}


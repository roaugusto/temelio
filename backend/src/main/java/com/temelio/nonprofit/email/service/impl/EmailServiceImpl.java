package com.temelio.nonprofit.email.service.impl;

import com.temelio.nonprofit.email.dto.ConfigDTO;
import com.temelio.nonprofit.email.dto.EmailSentChangeDTO;
import com.temelio.nonprofit.email.exception.BadRequestException;
import com.temelio.nonprofit.email.model.DonationEntity;
import com.temelio.nonprofit.email.repository.DonationRepository;
import com.temelio.nonprofit.email.service.ConfigService;
import com.temelio.nonprofit.email.service.EmailSentService;
import com.temelio.nonprofit.email.service.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

        private final DonationRepository donationRepository;
        private final ConfigService configService;
        private final LocalSendGridEmailService emailSender;
        private final EmailSentService emailSentService;

        public EmailServiceImpl(DonationRepository donationRepository, ConfigService configService, LocalSendGridEmailService emailSender, EmailSentService emailSentService) {
                this.donationRepository = donationRepository;
                this.configService = configService;
                this.emailSender = emailSender;
                this.emailSentService = emailSentService;
        }

        @Transactional
        public String sendDonationNotifications() {
                List<DonationEntity> pendingDonations = donationRepository.findByIsDonationNotifiedFalse();
                ConfigDTO config = configService.find();

                if (config == null) {
                        throw new BadRequestException("E-mail template not found.");
                }

                boolean allEmailsSentSuccessfully = true;

                for (DonationEntity donation : pendingDonations) {
                        String emailBody = formatEmailBody(config.getTemplate(), donation);
                        boolean email = emailSender.sendEmail(donation, config.getSubject(), emailBody);

                        if (email) {
                                donation.setIsDonationNotified(true);
                                donationRepository.save(donation);

                                EmailSentChangeDTO emailSent = new EmailSentChangeDTO();
                                emailSent.setOrganizationId(donation.getOrganization().getId());
                                emailSent.setOrganizationName(donation.getOrganization().getName());
                                emailSent.setOrganizationEmail(donation.getOrganization().getEmail());
                                emailSent.setSubject(config.getSubject());
                                emailSent.setContent(emailBody);
                                emailSent.setSendDate(LocalDateTime.now());
                                emailSentService.save(emailSent);
                        } else {
                                allEmailsSentSuccessfully = false;
                        }
                }

                return allEmailsSentSuccessfully ? "Email sent successfully" : "Failed to send all emails";
        }

        private String formatEmailBody(String template, DonationEntity donation) {
                return template
                        .replace("{name}", donation.getOrganization().getName())
                        .replace("{address}", donation.getOrganization().getAddress())
                        .replace("{amount}", donation.getAmount().toString());
        }
}

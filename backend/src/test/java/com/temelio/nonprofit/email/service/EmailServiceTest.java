package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.dto.ConfigDTO;
import com.temelio.nonprofit.email.dto.DonationDTO;
import com.temelio.nonprofit.email.dto.EmailSentDTO;
import com.temelio.nonprofit.email.exception.BadRequestException;
import com.temelio.nonprofit.email.model.ConfigEntity;
import com.temelio.nonprofit.email.model.DonationEntity;
import com.temelio.nonprofit.email.model.OrganizationEntity;
import com.temelio.nonprofit.email.repository.DonationRepository;
import com.temelio.nonprofit.email.service.impl.EmailServiceImpl;
import com.temelio.nonprofit.email.service.impl.LocalSendGridEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private ConfigService configService;

    @Mock
    private LocalSendGridEmailService emailSender;

    @Mock
    private EmailSentService emailSentService;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSendDonationNotifications() {
        OrganizationEntity organization = new OrganizationEntity(UUID.randomUUID(), "Organization 1", "Address 1", "Email 1", null);
        DonationEntity donation1 = new DonationEntity(UUID.randomUUID(), 150.0, null, organization);
        DonationEntity donation2 = new DonationEntity(UUID.randomUUID(), 200.0, null, organization);

        List<DonationEntity> pendingDonations = new ArrayList<>();
        pendingDonations.add(donation1);
        pendingDonations.add(donation2);

        ConfigDTO configDTO = new ConfigDTO(UUID.randomUUID(), "Config 1", "Subject 1", "Template 1");
        when(donationRepository.findByIsDonationNotifiedFalse()).thenReturn(pendingDonations);
        when(configService.find()).thenReturn(configDTO);
        when(emailSender.sendEmail(any(), anyString(), anyString())).thenReturn(true);
        when(emailSentService.save(any())).thenReturn(new EmailSentDTO());

        emailService.sendDonationNotifications();

        verify(donationRepository, times(1)).findByIsDonationNotifiedFalse();
        verify(configService, times(1)).find();
        verify(emailSender, times(2)).sendEmail(any(), any(), any());
        verify(donationRepository, times(2)).save(any());
    }

    @Test
    void testSendDonationNotificationsNoPendingDonations() {
        List<DonationEntity> pendingDonations = new ArrayList<>();
        ConfigDTO configDTO = new ConfigDTO();

        when(configService.find()).thenReturn(configDTO);
        when(donationRepository.findByIsDonationNotifiedFalse()).thenReturn(pendingDonations);

        emailService.sendDonationNotifications();

        verify(donationRepository, times(1)).findByIsDonationNotifiedFalse();
        verify(configService, times(1)).find();
        verify(emailSender, never()).sendEmail(any(), any(), any());
        verify(donationRepository, never()).save(any());
    }

    @Test
    void testSendDonationNotificationsNoConfig() {
        List<DonationEntity> pendingDonations = new ArrayList<>();
        pendingDonations.add(new DonationEntity());
        pendingDonations.add(new DonationEntity());

        when(donationRepository.findByIsDonationNotifiedFalse()).thenReturn(pendingDonations);
        when(configService.find()).thenReturn(null);

        assertThrows(BadRequestException.class, () -> emailService.sendDonationNotifications());


        verify(donationRepository, times(1)).findByIsDonationNotifiedFalse();
        verify(configService, times(1)).find();
        verify(emailSender, never()).sendEmail(any(), any(), any());
        verify(donationRepository, never()).save(any());
    }
}

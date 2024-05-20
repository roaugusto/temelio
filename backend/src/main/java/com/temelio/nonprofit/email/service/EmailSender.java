package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.dto.ConfigDTO;
import com.temelio.nonprofit.email.model.DonationEntity;

public interface EmailSender {
    boolean sendEmail(DonationEntity donation, String subject, String emailBody);
}

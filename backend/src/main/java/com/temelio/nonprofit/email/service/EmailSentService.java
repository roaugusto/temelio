package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.dto.EmailSentChangeDTO;
import com.temelio.nonprofit.email.dto.EmailSentDTO;

import java.util.List;
import java.util.UUID;

public interface EmailSentService {

    EmailSentDTO save(EmailSentChangeDTO emailSent);

    EmailSentDTO findById(UUID id);

    List<EmailSentDTO> findAll();


}

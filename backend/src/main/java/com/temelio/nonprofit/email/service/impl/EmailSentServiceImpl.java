package com.temelio.nonprofit.email.service.impl;

import com.temelio.nonprofit.email.dto.EmailSentChangeDTO;
import com.temelio.nonprofit.email.dto.EmailSentDTO;
import com.temelio.nonprofit.email.model.EmailSentEntity;
import com.temelio.nonprofit.email.repository.EmailSentRepository;
import com.temelio.nonprofit.email.service.EmailSentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailSentServiceImpl implements EmailSentService {

    private final EmailSentRepository emailSentRepository;
    ModelMapper modelMapper = new ModelMapper();

    public EmailSentServiceImpl(EmailSentRepository emailSentRepository) {
        this.emailSentRepository = emailSentRepository;
    }

    @Override
    public EmailSentDTO save(EmailSentChangeDTO emailSentChange) {

        EmailSentEntity emailSent = new EmailSentEntity();
        emailSent.setOrganizationId(emailSentChange.getOrganizationId());
        emailSent.setOrganizationName(emailSentChange.getOrganizationName());
        emailSent.setOrganizationEmail(emailSentChange.getOrganizationEmail());
        emailSent.setSubject(emailSentChange.getSubject());
        emailSent.setContent(emailSentChange.getContent());
        emailSent.setSendDate(emailSentChange.getSendDate());

        return mapToDTO(emailSentRepository.save(emailSent));
    }

    @Override
    public EmailSentDTO findById(UUID id) {
        Optional<EmailSentEntity> emailSentEntityOptional = emailSentRepository.findById(id);
        return emailSentEntityOptional.map(this::mapToDTO).orElse(null);
    }

    @Override
    public List<EmailSentDTO> findAll() {
        List<EmailSentEntity> users = (List<EmailSentEntity>) emailSentRepository.findAll();
        return users.stream().map(user ->
                modelMapper.map(user, EmailSentDTO.class)).toList();
    }

    private EmailSentDTO mapToDTO(EmailSentEntity emailSentEntity) {
        return modelMapper.map(emailSentEntity, EmailSentDTO.class);
    }

}

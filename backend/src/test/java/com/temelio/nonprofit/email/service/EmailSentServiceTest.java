package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.dto.EmailSentChangeDTO;
import com.temelio.nonprofit.email.dto.EmailSentDTO;
import com.temelio.nonprofit.email.model.EmailSentEntity;
import com.temelio.nonprofit.email.repository.EmailSentRepository;
import com.temelio.nonprofit.email.service.impl.EmailSentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailSentServiceTest {

    @Mock
    private EmailSentRepository emailSentRepository;

    @InjectMocks
    private EmailSentServiceImpl emailSentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        EmailSentChangeDTO emailSentChangeDTO = new EmailSentChangeDTO();
        emailSentChangeDTO.setOrganizationId(UUID.randomUUID());
        emailSentChangeDTO.setOrganizationName("Organization Name");
        emailSentChangeDTO.setOrganizationEmail("email@organization.com");
        emailSentChangeDTO.setSubject("Subject");
        emailSentChangeDTO.setContent("Content");
        emailSentChangeDTO.setSendDate(LocalDateTime.now());

        EmailSentEntity emailSentEntity = new EmailSentEntity();
        emailSentEntity.setId(UUID.randomUUID());
        emailSentEntity.setOrganizationId(emailSentChangeDTO.getOrganizationId());
        emailSentEntity.setOrganizationName(emailSentChangeDTO.getOrganizationName());
        emailSentEntity.setOrganizationEmail(emailSentChangeDTO.getOrganizationEmail());
        emailSentEntity.setSubject(emailSentChangeDTO.getSubject());
        emailSentEntity.setContent(emailSentChangeDTO.getContent());
        emailSentEntity.setSendDate(emailSentChangeDTO.getSendDate());

        when(emailSentRepository.save(any(EmailSentEntity.class))).thenReturn(emailSentEntity);

        EmailSentDTO result = emailSentService.save(emailSentChangeDTO);

        assertNotNull(result);
        assertEquals(emailSentEntity.getId(), result.getId());
        assertEquals(emailSentEntity.getOrganizationId(), result.getOrganizationId());
        assertEquals(emailSentEntity.getOrganizationName(), result.getOrganizationName());
        assertEquals(emailSentEntity.getOrganizationEmail(), result.getOrganizationEmail());
        assertEquals(emailSentEntity.getSubject(), result.getSubject());
        assertEquals(emailSentEntity.getContent(), result.getContent());
        assertEquals(emailSentEntity.getSendDate(), result.getSendDate());
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        EmailSentEntity emailSentEntity = new EmailSentEntity();
        emailSentEntity.setId(id);
        emailSentEntity.setOrganizationId(UUID.randomUUID());
        emailSentEntity.setOrganizationName("Organization Name");
        emailSentEntity.setOrganizationEmail("email@organization.com");
        emailSentEntity.setSubject("Subject");
        emailSentEntity.setContent("Content");
        emailSentEntity.setSendDate(LocalDateTime.now());

        when(emailSentRepository.findById(id)).thenReturn(Optional.of(emailSentEntity));

        EmailSentDTO result = emailSentService.findById(id);

        assertNotNull(result);
        assertEquals(emailSentEntity.getId(), result.getId());
        assertEquals(emailSentEntity.getOrganizationId(), result.getOrganizationId());
        assertEquals(emailSentEntity.getOrganizationName(), result.getOrganizationName());
        assertEquals(emailSentEntity.getOrganizationEmail(), result.getOrganizationEmail());
        assertEquals(emailSentEntity.getSubject(), result.getSubject());
        assertEquals(emailSentEntity.getContent(), result.getContent());
        assertEquals(emailSentEntity.getSendDate(), result.getSendDate());
    }

    @Test
    void testFindById_NotFound() {
        UUID id = UUID.randomUUID();
        when(emailSentRepository.findById(id)).thenReturn(Optional.empty());

        EmailSentDTO result = emailSentService.findById(id);

        assertNull(result);
    }

    @Test
    void testFindAll() {
        EmailSentEntity emailSentEntity = new EmailSentEntity();
        emailSentEntity.setId(UUID.randomUUID());
        emailSentEntity.setOrganizationId(UUID.randomUUID());
        emailSentEntity.setOrganizationName("Organization Name");
        emailSentEntity.setOrganizationEmail("email@organization.com");
        emailSentEntity.setSubject("Subject");
        emailSentEntity.setContent("Content");
        emailSentEntity.setSendDate(LocalDateTime.now());

        when(emailSentRepository.findAll()).thenReturn(Collections.singletonList(emailSentEntity));

        List<EmailSentDTO> result = emailSentService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        EmailSentDTO emailSentDTO = result.get(0);
        assertEquals(emailSentEntity.getId(), emailSentDTO.getId());
        assertEquals(emailSentEntity.getOrganizationId(), emailSentDTO.getOrganizationId());
        assertEquals(emailSentEntity.getOrganizationName(), emailSentDTO.getOrganizationName());
        assertEquals(emailSentEntity.getOrganizationEmail(), emailSentDTO.getOrganizationEmail());
        assertEquals(emailSentEntity.getSubject(), emailSentDTO.getSubject());
        assertEquals(emailSentEntity.getContent(), emailSentDTO.getContent());
        assertEquals(emailSentEntity.getSendDate(), emailSentDTO.getSendDate());
    }
}

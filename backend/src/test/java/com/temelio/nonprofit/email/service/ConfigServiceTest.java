package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.dto.ConfigChangeDTO;
import com.temelio.nonprofit.email.dto.ConfigDTO;
import com.temelio.nonprofit.email.exception.BadRequestException;
import com.temelio.nonprofit.email.model.ConfigEntity;
import com.temelio.nonprofit.email.repository.ConfigRepository;
import com.temelio.nonprofit.email.service.impl.ConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConfigServiceTest {

    @Mock
    private ConfigRepository configRepository;

    @InjectMocks
    private ConfigServiceImpl configService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        ConfigChangeDTO configChangeDTO = new ConfigChangeDTO();
        configChangeDTO.setName("Config 1");
        configChangeDTO.setSubject("Subject 1");
        configChangeDTO.setTemplate("Template 1");

        ConfigEntity savedConfigEntity = new ConfigEntity(UUID.randomUUID(), "Config 1", "Subject 1", "Template 1");

        when(configRepository.save(any(ConfigEntity.class))).thenReturn(savedConfigEntity);
        ConfigDTO result = configService.save(configChangeDTO);

        assertNotNull(result);
        assertEquals(savedConfigEntity.getId(), result.getId());
        assertEquals(savedConfigEntity.getName(), result.getName());
        assertEquals(savedConfigEntity.getSubject(), result.getSubject());
        assertEquals(savedConfigEntity.getTemplate(), result.getTemplate());
    }

    @Test
    void testFind() {
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setId(UUID.randomUUID());
        configEntity.setName("Config 1");
        configEntity.setSubject("Subject 1");
        configEntity.setTemplate("Template 1");

        when(configRepository.findAll()).thenReturn(Collections.singletonList(configEntity));

        ConfigDTO result = configService.find();

        assertNotNull(result);
        assertEquals(configEntity.getId(), result.getId());
        assertEquals(configEntity.getName(), result.getName());
        assertEquals(configEntity.getSubject(), result.getSubject());
        assertEquals(configEntity.getTemplate(), result.getTemplate());
    }

    @Test
    void testUpdate() {
        UUID configId = UUID.randomUUID();

        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setId(configId);
        configEntity.setName("Config 1");
        configEntity.setSubject("Subject 1");
        configEntity.setTemplate("Template 1");

        when(configRepository.findAll()).thenReturn(Collections.singletonList(configEntity));

        ConfigChangeDTO configChangeDTO = new ConfigChangeDTO();
        configChangeDTO.setName("Updated Config");
        configChangeDTO.setSubject("Updated Subject");
        configChangeDTO.setTemplate("Updated Template");

        ConfigEntity existingConfigEntity = new ConfigEntity(configId, "Config 1", "Subject 1", "Template 1");
        when(configRepository.findById(configId)).thenReturn(Optional.of(existingConfigEntity));
        when(configRepository.save(any(ConfigEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ConfigDTO updatedConfig = configService.update(configChangeDTO);

        assertNotNull(updatedConfig);
        assertEquals(configId, updatedConfig.getId());
        assertEquals("Updated Config", updatedConfig.getName());
        assertEquals("Updated Subject", updatedConfig.getSubject());
        assertEquals("Updated Template", updatedConfig.getTemplate());
    }

    @Test
    void testUpdateConfigNotFound() {
        UUID configId = UUID.randomUUID();
        ConfigChangeDTO configChangeDTO = new ConfigChangeDTO();
        configChangeDTO.setName("Updated Config");
        configChangeDTO.setSubject("Updated Subject");
        configChangeDTO.setTemplate("Updated Template");

        when(configRepository.findById(configId)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> configService.update(configChangeDTO));
    }

    @Test
    void testDelete() {
        UUID configId = UUID.randomUUID();
        ConfigEntity existingConfigEntity = new ConfigEntity(configId, "Config 1", "Subject 1", "Template 1");

        when(configRepository.findAll()).thenReturn(Collections.singletonList(existingConfigEntity));
        configService.delete();
        verify(configRepository, times(1)).deleteById(configId);
    }

    @Test
    void testDeleteConfigNotFound() {
        when(configRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(BadRequestException.class, () -> configService.delete());
    }

}

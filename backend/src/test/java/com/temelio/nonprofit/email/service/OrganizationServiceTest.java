package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.dto.OrganizationChangeDTO;
import com.temelio.nonprofit.email.dto.OrganizationDTO;
import com.temelio.nonprofit.email.exception.BadRequestException;
import com.temelio.nonprofit.email.model.OrganizationEntity;
import com.temelio.nonprofit.email.repository.OrganizationRepository;
import com.temelio.nonprofit.email.service.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveOrganization() {
        OrganizationChangeDTO organizationDTO = new OrganizationChangeDTO();
        organizationDTO.setName("Test Organization");
        OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setId(UUID.randomUUID());
        organizationEntity.setName("Test Organization");

        when(organizationRepository.save(any())).thenReturn(organizationEntity);

        OrganizationDTO savedOrganization = organizationService.save(organizationDTO);

        assertNotNull(savedOrganization);
        assertEquals(organizationEntity.getId(), savedOrganization.getId());
        assertEquals(organizationDTO.getName(), savedOrganization.getName());
        verify(organizationRepository, times(1)).save(any());
    }

    @Test
    void testFindOrganizationById() {
        UUID id = UUID.randomUUID();
        OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setId(id);
        organizationEntity.setName("Test Organization");

        when(organizationRepository.findById(id)).thenReturn(Optional.of(organizationEntity));

        OrganizationDTO foundOrganization = organizationService.findById(id);

        assertNotNull(foundOrganization);
        assertEquals(id, foundOrganization.getId());
        assertEquals(organizationEntity.getName(), foundOrganization.getName());
        verify(organizationRepository, times(1)).findById(id);
    }

    @Test
    void testFindAllOrganizations() {
        List<OrganizationEntity> organizationEntities = List.of(
                new OrganizationEntity(UUID.randomUUID(), "Organization 1", "Address 1", "Email 1", null),
                new OrganizationEntity(UUID.randomUUID(), "Organization 2", "Address 2", "Email 2", null)
        );

        when(organizationRepository.findAll()).thenReturn(organizationEntities);

        List<OrganizationDTO> organizationDTOs = organizationService.findAll();

        assertNotNull(organizationDTOs);
        assertEquals(organizationEntities.size(), organizationDTOs.size());
        for (int i = 0; i < organizationEntities.size(); i++) {
            assertEquals(organizationEntities.get(i).getId(), organizationDTOs.get(i).getId());
            assertEquals(organizationEntities.get(i).getName(), organizationDTOs.get(i).getName());
        }
        verify(organizationRepository, times(1)).findAll();
    }

    @Test
    void testUpdateOrganization() {
        UUID id = UUID.randomUUID();
        OrganizationChangeDTO organizationDTO = new OrganizationChangeDTO();
        organizationDTO.setName("Updated Organization");
        OrganizationEntity existingOrganization = new OrganizationEntity(id, "Organization 1", "Address 1", "Email 1", null);

        when(organizationRepository.findById(id)).thenReturn(Optional.of(existingOrganization));
        when(organizationRepository.save(any())).thenReturn(existingOrganization);

        OrganizationDTO updatedOrganization = organizationService.update(id, organizationDTO);

        assertNotNull(updatedOrganization);
        assertEquals(id, updatedOrganization.getId());
        assertEquals(organizationDTO.getName(), updatedOrganization.getName());
        verify(organizationRepository, times(1)).findById(id);
        verify(organizationRepository, times(1)).save(any());
    }

    @Test
    void testDeleteOrganizationById() {
        UUID id = UUID.randomUUID();
        OrganizationEntity existingOrganization = new OrganizationEntity(id, "Organization 1", "Address 1", "Email 1", null);

        when(organizationRepository.findById(id)).thenReturn(Optional.of(existingOrganization));

        organizationService.deleteById(id);

        verify(organizationRepository, times(1)).findById(id);
        verify(organizationRepository, times(1)).deleteById(id);
    }


    @Test
    void testUpdateOrganizationWhenIdNotFound() {
        UUID id = UUID.randomUUID();
        OrganizationChangeDTO organizationDTO = new OrganizationChangeDTO();
        organizationDTO.setName("Updated Organization");

        when(organizationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> organizationService.update(id, organizationDTO));
        verify(organizationRepository, times(1)).findById(id);
        verify(organizationRepository, never()).save(any());
    }

    @Test
    void testDeleteOrganizationByIdWhenIdNotFound() {
        UUID id = UUID.randomUUID();

        when(organizationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> organizationService.deleteById(id));
        verify(organizationRepository, times(1)).findById(id);
        verify(organizationRepository, never()).deleteById(id);
    }
}

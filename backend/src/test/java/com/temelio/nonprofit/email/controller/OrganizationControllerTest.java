package com.temelio.nonprofit.email.controller;

import com.temelio.nonprofit.email.dto.OrganizationChangeDTO;
import com.temelio.nonprofit.email.dto.OrganizationDTO;
import com.temelio.nonprofit.email.dto.Response;
import com.temelio.nonprofit.email.service.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrganizationControllerTest {

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OrganizationController organizationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveOrganization() {
        OrganizationChangeDTO organizationDTO = new OrganizationChangeDTO();
        organizationDTO.setName("Test Organization");
        OrganizationDTO savedOrganizationDTO = new OrganizationDTO();
        savedOrganizationDTO.setId(UUID.randomUUID());
        savedOrganizationDTO.setName("Test Organization");

        when(organizationService.save(any(OrganizationChangeDTO.class))).thenReturn(savedOrganizationDTO);

        ResponseEntity<Response<OrganizationDTO>> responseEntity = organizationController.saveOrganization(organizationDTO);
        Response<OrganizationDTO> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(savedOrganizationDTO, response.getData());
    }

    @Test
    void testGetOrganizationById() {
        UUID id = UUID.randomUUID();
        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setId(id);
        organizationDTO.setName("Test Organization");

        when(organizationService.findById(id)).thenReturn(organizationDTO);

        ResponseEntity<Response<OrganizationDTO>> responseEntity = organizationController.getOrganizationById(id);
        Response<OrganizationDTO> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(organizationDTO, response.getData());
    }

    @Test
    void testGetAllOrganizations() {
        OrganizationDTO organization1 = new OrganizationDTO();
        organization1.setId(UUID.randomUUID());
        organization1.setName("Organization 1");

        OrganizationDTO organization2 = new OrganizationDTO();
        organization2.setId(UUID.randomUUID());
        organization2.setName("Organization 2");

        List<OrganizationDTO> organizations = List.of(organization1, organization2);

        when(organizationService.findAll()).thenReturn(organizations);

        ResponseEntity<Response<List<OrganizationDTO>>> responseEntity = organizationController.getAllOrganizations();
        Response<List<OrganizationDTO>> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(organizations, response.getData());
    }

    @Test
    void testUpdateOrganization() {
        UUID id = UUID.randomUUID();
        OrganizationChangeDTO organizationChangeDTO = new OrganizationChangeDTO();
        organizationChangeDTO.setName("Updated Organization");

        OrganizationDTO updatedOrganizationDTO = new OrganizationDTO();
        updatedOrganizationDTO.setId(id);
        updatedOrganizationDTO.setName("Updated Organization");

        when(organizationService.update(any(UUID.class), any(OrganizationChangeDTO.class))).thenReturn(updatedOrganizationDTO);

        ResponseEntity<Response<OrganizationDTO>> responseEntity = organizationController.updateOrganization(id, organizationChangeDTO);
        Response<OrganizationDTO> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(updatedOrganizationDTO, response.getData());
    }

    @Test
    void testDeleteOrganization() {
        UUID id = UUID.randomUUID();

        doNothing().when(organizationService).deleteById(id);

        ResponseEntity<Response<Void>> responseEntity = organizationController.deleteOrganization(id);
        Response<Void> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode());
        assertEquals(null, response.getData());
    }
}

package com.temelio.nonprofit.email.controller;

import com.temelio.nonprofit.email.dto.DonationChangeDTO;
import com.temelio.nonprofit.email.dto.DonationDTO;
import com.temelio.nonprofit.email.dto.Response;
import com.temelio.nonprofit.email.model.OrganizationEntity;
import com.temelio.nonprofit.email.service.DonationService;
import com.temelio.nonprofit.email.service.EmailService;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class DonationControllerTest {

    @Mock
    private DonationService donationService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private DonationController donationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveDonation() {
        UUID organizationId = UUID.randomUUID();
        Double amount = 100.0;
        DonationDTO donationDTO = new DonationDTO();
        donationDTO.setId(UUID.randomUUID());
        donationDTO.setAmount(amount);
        donationDTO.setOrganization(new OrganizationEntity(UUID.randomUUID(), "Organization 1", "Address 1", "Email 1", null));

        when(donationService.save(organizationId, amount)).thenReturn(donationDTO);

        ResponseEntity<Response<DonationDTO>> responseEntity = donationController.saveDonation(new DonationChangeDTO(amount, organizationId, false));
        Response<DonationDTO> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals(donationDTO, response.getData());
    }

    @Test
    void testGetDonationById() {
        UUID id = UUID.randomUUID();
        DonationDTO donationDTO = new DonationDTO();
        donationDTO.setId(id);
        donationDTO.setAmount(100.0);
        donationDTO.setOrganization(new OrganizationEntity(UUID.randomUUID(), "Organization 1", "Address 1", "Email 1", null));

        when(donationService.findById(id)).thenReturn(donationDTO);

        ResponseEntity<Response<DonationDTO>> responseEntity = donationController.getDonationById(id);
        Response<DonationDTO> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(donationDTO, response.getData());
    }

    @Test
    void testGetDonationByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(donationService.findById(id)).thenReturn(null);

        ResponseEntity<Response<DonationDTO>> responseEntity = donationController.getDonationById(id);
        Response<DonationDTO> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void testGetAllDonations() {
        List<DonationDTO> donations = List.of(
                new DonationDTO(UUID.randomUUID(), 100.0, new OrganizationEntity(UUID.randomUUID(), "Organization 1", "Address 1", "Email 1", null), false),
                new DonationDTO(UUID.randomUUID(), 200.0, new OrganizationEntity(UUID.randomUUID(), "Organization 1", "Address 1", "Email 1", null), false)
        );

        when(donationService.findAll()).thenReturn(donations);

        ResponseEntity<Response<List<DonationDTO>>> responseEntity = donationController.getAllDonations();
        Response<List<DonationDTO>> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(donations, response.getData());
    }

    @Test
    void testUpdateDonation() {
        UUID id = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        Double amount = 150.0;
        boolean isDonationNotified = false;

        DonationDTO updatedDonationDTO = new DonationDTO();
        updatedDonationDTO.setId(id);
        updatedDonationDTO.setAmount(amount);
        updatedDonationDTO.setOrganization(new OrganizationEntity(UUID.randomUUID(), "Organization 1", "Address 1", "Email 1", null));

        when(donationService.update(id, organizationId, amount, isDonationNotified)).thenReturn(updatedDonationDTO);

        ResponseEntity<Response<DonationDTO>> responseEntity = donationController.updateDonation(id, new DonationChangeDTO(amount, organizationId, isDonationNotified));
        Response<DonationDTO> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(updatedDonationDTO, response.getData());
    }

    @Test
    void testDeleteDonation() {
        UUID id = UUID.randomUUID();

        doNothing().when(donationService).deleteById(id);

        ResponseEntity<Response<Void>> responseEntity = donationController.deleteDonation(id);
        Response<Void> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void testSendEmailSuccess() {
        when(emailService.sendDonationNotifications()).thenReturn("Email sent successfully");
        ResponseEntity<Response<String>> responseEntity = donationController.sendEmail();

        Response<String> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Email sent successfully", response.getMessage());
    }

    @Test
    void testSendEmailFailure() {
        when(emailService.sendDonationNotifications()).thenReturn("Failed to send all emails");
        ResponseEntity<Response<String>> responseEntity = donationController.sendEmail();

        Response<String> response = responseEntity.getBody();

        assert response != null;
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Failed to send all emails", response.getMessage());
    }
}

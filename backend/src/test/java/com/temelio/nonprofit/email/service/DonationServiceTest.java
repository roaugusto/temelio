package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.dto.DonationDTO;
import com.temelio.nonprofit.email.exception.BadRequestException;
import com.temelio.nonprofit.email.model.DonationEntity;
import com.temelio.nonprofit.email.model.OrganizationEntity;
import com.temelio.nonprofit.email.repository.DonationRepository;
import com.temelio.nonprofit.email.repository.OrganizationRepository;
import com.temelio.nonprofit.email.service.impl.DonationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DonationServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private DonationServiceImpl donationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveDonation() {
        UUID organizationId = UUID.randomUUID();
        Double amount = 100.0;
        OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setId(organizationId);
        DonationEntity donationEntity = new DonationEntity(UUID.randomUUID(), amount, false, organizationEntity);

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organizationEntity));
        when(donationRepository.save(any(DonationEntity.class))).thenReturn(donationEntity);

        DonationDTO savedDonation = donationService.save(organizationId, amount);

        assertNotNull(savedDonation);
        assertEquals(donationEntity.getId(), savedDonation.getId());
        assertEquals(amount, savedDonation.getAmount());
        assertEquals(organizationId, savedDonation.getOrganization().getId());
        verify(organizationRepository, times(1)).findById(organizationId);
        verify(donationRepository, times(1)).save(any(DonationEntity.class));
    }

    @Test
    void testSaveDonationOrganizationNotFound() {
        UUID organizationId = UUID.randomUUID();
        Double amount = 100.0;

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> donationService.save(organizationId, amount));
        verify(organizationRepository, times(1)).findById(organizationId);
        verify(donationRepository, never()).save(any(DonationEntity.class));
    }

    @Test
    void testFindDonationById() {
        UUID id = UUID.randomUUID();
        OrganizationEntity organizationEntity = new OrganizationEntity();
        DonationEntity donationEntity = new DonationEntity(id, 100.0, false, organizationEntity);

        when(donationRepository.findById(id)).thenReturn(Optional.of(donationEntity));

        DonationDTO foundDonation = donationService.findById(id);

        assertNotNull(foundDonation);
        assertEquals(id, foundDonation.getId());
        assertEquals(donationEntity.getAmount(), foundDonation.getAmount());
        verify(donationRepository, times(1)).findById(id);
    }

    @Test
    void testFindDonationByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(donationRepository.findById(id)).thenReturn(Optional.empty());

        DonationDTO foundDonation = donationService.findById(id);

        assertNull(foundDonation);
        verify(donationRepository, times(1)).findById(id);
    }

    @Test
    void testFindAllDonations() {
        OrganizationEntity organizationEntity = new OrganizationEntity();
        DonationEntity donationEntity1 = new DonationEntity(UUID.randomUUID(), 100.0, false, organizationEntity);
        DonationEntity donationEntity2 = new DonationEntity(UUID.randomUUID(), 200.0, false, organizationEntity);

        when(donationRepository.findAll()).thenReturn(List.of(donationEntity1, donationEntity2));

        List<DonationDTO> donations = donationService.findAll();

        assertEquals(2, donations.size());
        verify(donationRepository, times(1)).findAll();
    }

    @Test
    void testUpdateDonation() {
        UUID donationId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        Double newAmount = 200.0;
        OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setId(organizationId);
        DonationEntity existingDonation = new DonationEntity(donationId, 100.0, false, organizationEntity);

        when(donationRepository.findById(donationId)).thenReturn(Optional.of(existingDonation));
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organizationEntity));
        when(donationRepository.save(any(DonationEntity.class))).thenReturn(existingDonation);

        DonationDTO updatedDonation = donationService.update(donationId, organizationId, newAmount, false);

        assertNotNull(updatedDonation);
        assertEquals(donationId, updatedDonation.getId());
        assertEquals(newAmount, updatedDonation.getAmount());
        assertEquals(organizationId, updatedDonation.getOrganization().getId());
        verify(donationRepository, times(1)).findById(donationId);
        verify(organizationRepository, times(1)).findById(organizationId);
        verify(donationRepository, times(1)).save(any(DonationEntity.class));
    }

    @Test
    void testUpdateDonationNotFound() {
        UUID donationId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        Double newAmount = 200.0;
        boolean isDonationNotified = false;

        when(donationRepository.findById(donationId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> donationService.update(donationId, organizationId, newAmount, isDonationNotified));
        verify(donationRepository, times(1)).findById(donationId);
        verify(organizationRepository, never()).findById(any(UUID.class));
        verify(donationRepository, never()).save(any(DonationEntity.class));
    }

    @Test
    void testUpdateDonationOrganizationNotFound() {
        UUID donationId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        Double newAmount = 200.0;
        boolean isDonationNotified = false;

        DonationEntity existingDonation = new DonationEntity(donationId, 100.0, isDonationNotified, new OrganizationEntity());

        when(donationRepository.findById(donationId)).thenReturn(Optional.of(existingDonation));
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> donationService.update(donationId, organizationId, newAmount, isDonationNotified));
        verify(donationRepository, times(1)).findById(donationId);
        verify(organizationRepository, times(1)).findById(organizationId);
        verify(donationRepository, never()).save(any(DonationEntity.class));
    }

    @Test
    void testDeleteDonation() {
        UUID donationId = UUID.randomUUID();
        DonationEntity donationEntity = new DonationEntity(donationId, 100.0, false, new OrganizationEntity());

        when(donationRepository.findById(donationId)).thenReturn(Optional.of(donationEntity));

        donationService.deleteById(donationId);

        verify(donationRepository, times(1)).findById(donationId);
        verify(donationRepository, times(1)).deleteById(donationId);
    }

    @Test
    void testDeleteDonationNotFound() {
        UUID donationId = UUID.randomUUID();

        when(donationRepository.findById(donationId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> donationService.deleteById(donationId));
        verify(donationRepository, times(1)).findById(donationId);
        verify(donationRepository, never()).deleteById(donationId);
    }

    @Test
    void testFindUnnotifiedDonations() {
        OrganizationEntity organizationEntity = new OrganizationEntity();
        DonationEntity donationEntity1 = new DonationEntity(UUID.randomUUID(), 100.0, false, organizationEntity);
        DonationEntity donationEntity2 = new DonationEntity(UUID.randomUUID(), 200.0, true, organizationEntity);
        DonationEntity donationEntity3 = new DonationEntity(UUID.randomUUID(), 300.0, false, organizationEntity);

        when(donationRepository.findByIsDonationNotifiedFalse()).thenReturn(List.of(donationEntity1, donationEntity3));

        List<DonationEntity> unnotifiedDonations = donationService.findUnnotifiedDonations();

        assertEquals(2, unnotifiedDonations.size());
        assertFalse(unnotifiedDonations.contains(donationEntity2));
        verify(donationRepository, times(1)).findByIsDonationNotifiedFalse();
    }

}

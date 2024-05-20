package com.temelio.nonprofit.email.service.impl;

import com.temelio.nonprofit.email.dto.DonationChangeDTO;
import com.temelio.nonprofit.email.dto.DonationDTO;
import com.temelio.nonprofit.email.exception.BadRequestException;
import com.temelio.nonprofit.email.model.DonationEntity;
import com.temelio.nonprofit.email.model.OrganizationEntity;
import com.temelio.nonprofit.email.repository.DonationRepository;
import com.temelio.nonprofit.email.repository.OrganizationRepository;
import com.temelio.nonprofit.email.service.DonationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final OrganizationRepository organizationRepository;

    ModelMapper modelMapper = new ModelMapper();

    public DonationServiceImpl(DonationRepository donationRepository, OrganizationRepository organizationRepository) {
        this.donationRepository = donationRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public DonationDTO save(UUID organizationId, Double amount) {
        OrganizationEntity organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new BadRequestException(String.format("Organization with id %s not found!", organizationId)));

        DonationEntity donation = new DonationEntity();
        donation.setOrganization(organization);
        donation.setAmount(amount);
        donation.setIsDonationNotified(false);

        return mapToDTO(donationRepository.save(donation));
    }

    @Override
    public DonationDTO findById(UUID id) {
        Optional<DonationEntity> donationEntityOptional = donationRepository.findById(id);
        return donationEntityOptional.map(this::mapToDTO).orElse(null);
    }

    @Override
    public List<DonationDTO> findAll() {
        List<DonationEntity> users = (List<DonationEntity>) donationRepository.findAll();
        return users.stream().map(user ->
                modelMapper.map(user, DonationDTO.class)).toList();
    }

    @Override
    public DonationDTO update(UUID id, UUID organizationId, Double amount, boolean isDonationNotified) {
        DonationEntity existingDonation = donationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(String.format("Donation with id %s not found!", id)));

        OrganizationEntity organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new BadRequestException(String.format("Organization with id %s not found!", organizationId)));

        existingDonation.setOrganization(organization);
        existingDonation.setAmount(amount);
        existingDonation.setIsDonationNotified(isDonationNotified);

        return mapToDTO(donationRepository.save(existingDonation));
    }

    @Override
    public void deleteById(UUID id) {
        Optional<DonationEntity> existingDonationOptional = donationRepository.findById(id);
        if (existingDonationOptional.isEmpty()) {
            throw new BadRequestException(String.format("Donation with id %s not found!", id));
        }
        donationRepository.deleteById(id);
    }

    @Override
    public List<DonationEntity> findUnnotifiedDonations() {
        return donationRepository.findByIsDonationNotifiedFalse();
    }

    private DonationDTO mapToDTO(DonationEntity donationEntity) {
        return modelMapper.map(donationEntity, DonationDTO.class);
    }

}

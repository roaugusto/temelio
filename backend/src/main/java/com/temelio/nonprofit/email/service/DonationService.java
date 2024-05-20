package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.dto.DonationDTO;
import com.temelio.nonprofit.email.model.DonationEntity;

import java.util.List;
import java.util.UUID;

public interface DonationService {

    DonationDTO save(UUID organizationId, Double amount);

    DonationDTO findById(UUID id);

    List<DonationDTO> findAll();

    DonationDTO update(UUID id, UUID organizationId, Double amount, boolean isDonationNotified);

    void deleteById(UUID id);

    List<DonationEntity> findUnnotifiedDonations();

}

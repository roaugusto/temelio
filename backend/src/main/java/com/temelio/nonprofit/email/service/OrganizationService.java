package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.dto.OrganizationChangeDTO;
import com.temelio.nonprofit.email.dto.OrganizationDTO;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    OrganizationDTO save(OrganizationChangeDTO organizationDTO);

    OrganizationDTO findById(UUID id);

    List<OrganizationDTO> findAll();

    OrganizationDTO update(UUID id, OrganizationChangeDTO organizationDTO);

    void deleteById(UUID id);

}

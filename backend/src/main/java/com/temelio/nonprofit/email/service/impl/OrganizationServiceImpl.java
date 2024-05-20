package com.temelio.nonprofit.email.service.impl;

import com.temelio.nonprofit.email.dto.OrganizationChangeDTO;
import com.temelio.nonprofit.email.dto.OrganizationDTO;
import com.temelio.nonprofit.email.exception.BadRequestException;
import com.temelio.nonprofit.email.model.DonationEntity;
import com.temelio.nonprofit.email.model.OrganizationEntity;
import com.temelio.nonprofit.email.repository.OrganizationRepository;
import com.temelio.nonprofit.email.service.OrganizationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    ModelMapper modelMapper = new ModelMapper();

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public OrganizationDTO save(OrganizationChangeDTO organizationDTO) {
        OrganizationEntity organization = modelMapper.map(organizationDTO, OrganizationEntity.class);
        return  mapToDTO(organizationRepository.save(organization));
    }

    @Override
    public OrganizationDTO findById(UUID id) {
        Optional<OrganizationEntity> organization = organizationRepository.findById(id);
        return organization.map(this::mapToDTO).orElse(null);
    }

    @Override
    public List<OrganizationDTO> findAll() {
        List<OrganizationEntity> users = (List<OrganizationEntity>) organizationRepository.findAll();
        return users.stream().map(user ->
                modelMapper.map(user, OrganizationDTO.class)).toList();
    }

    @Override
    public OrganizationDTO update(UUID id, OrganizationChangeDTO organizationDTO) {
        Optional<OrganizationEntity> existingOrganizationOptional = organizationRepository.findById(id);
        if (existingOrganizationOptional.isPresent()) {
            OrganizationEntity existingOrganization = existingOrganizationOptional.get();
            existingOrganization.setName(organizationDTO.getName());
            existingOrganization.setAddress(organizationDTO.getAddress());
            existingOrganization.setEmail(organizationDTO.getEmail());
            return mapToDTO(organizationRepository.save(existingOrganization));
        } else {
            throw new BadRequestException(String.format("Organization with id %s not found!", id));
        }
    }

    @Override
    public void deleteById(UUID id) {
        Optional<OrganizationEntity> existingOrganizationOptional = organizationRepository.findById(id);
        if (existingOrganizationOptional.isEmpty()) {
            throw new BadRequestException(String.format("Organization with id %s not found!", id));
        }
        organizationRepository.deleteById(id);
    }

    private OrganizationDTO mapToDTO(OrganizationEntity organizationEntity) {
        return modelMapper.map(organizationEntity, OrganizationDTO.class);
    }

}

package com.temelio.nonprofit.email.service.impl;

import com.temelio.nonprofit.email.dto.ConfigChangeDTO;
import com.temelio.nonprofit.email.dto.ConfigDTO;
import com.temelio.nonprofit.email.exception.BadRequestException;
import com.temelio.nonprofit.email.model.ConfigEntity;
import com.temelio.nonprofit.email.repository.ConfigRepository;
import com.temelio.nonprofit.email.service.ConfigService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository configRepository;

    ModelMapper modelMapper = new ModelMapper();

    public ConfigServiceImpl(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public ConfigDTO save(ConfigChangeDTO configDTO) {
        ConfigEntity config = modelMapper.map(configDTO, ConfigEntity.class);
        return  mapToDTO(configRepository.save(config));
    }

    @Override
    public ConfigDTO find() {
        List<ConfigEntity> configList = StreamSupport.stream(configRepository.findAll().spliterator(), false).toList();

        return configList.stream()
                .findFirst()
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    public ConfigDTO update(ConfigChangeDTO configDTO) {
        List<ConfigEntity> configList = StreamSupport.stream(configRepository.findAll().spliterator(), false).toList();
        Optional<ConfigEntity> config = configList.stream().findFirst();

        if (config.isPresent()) {
            ConfigEntity existingConfig = config.get();
            existingConfig.setName(configDTO.getName());
            existingConfig.setTemplate(configDTO.getTemplate());
            existingConfig.setSubject(configDTO.getSubject());
            return mapToDTO(configRepository.save(existingConfig));
        } else {
            throw new BadRequestException("Config not found!");
        }
    }

    @Override
    public void delete() {
        List<ConfigEntity> configList = StreamSupport.stream(configRepository.findAll().spliterator(), false).toList();
        Optional<ConfigEntity> config = configList.stream().findFirst();

        if (config.isEmpty()) {
            throw new BadRequestException("Config not found!");
        }
        configRepository.deleteById(config.get().getId());
    }

    private ConfigDTO mapToDTO(ConfigEntity configEntity) {
        return modelMapper.map(configEntity, ConfigDTO.class);
    }

}

package com.temelio.nonprofit.email.service;

import com.temelio.nonprofit.email.dto.ConfigChangeDTO;
import com.temelio.nonprofit.email.dto.ConfigDTO;

import java.util.UUID;

public interface ConfigService {

    ConfigDTO save(ConfigChangeDTO organizationDTO);

    ConfigDTO find();

    ConfigDTO update(ConfigChangeDTO organizationDTO);

    void delete();

}

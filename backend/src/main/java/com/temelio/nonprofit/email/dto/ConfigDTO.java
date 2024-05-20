package com.temelio.nonprofit.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDTO {

    private UUID id;
    private String name;
    private String subject;
    private String template;

}

package com.temelio.nonprofit.email.dto;

import com.temelio.nonprofit.email.model.OrganizationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationDTO {

    private UUID id;
    private Double amount;
    private OrganizationEntity organization;
    private Boolean isDonationNotified;

}

package com.temelio.nonprofit.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationChangeDTO {

    private Double amount;
    private UUID organizationId;
    private Boolean isDonationNotified;

}

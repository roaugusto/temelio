package com.temelio.nonprofit.email.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "donations")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DonationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "is_donation_notified")
    private Boolean isDonationNotified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    @JsonBackReference
    private OrganizationEntity organization;
}

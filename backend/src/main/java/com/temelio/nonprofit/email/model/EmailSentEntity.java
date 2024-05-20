package com.temelio.nonprofit.email.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "email_sent")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmailSentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "organization_email", nullable = false)
    private String organizationEmail;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sendDate;

}

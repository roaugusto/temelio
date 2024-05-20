package com.temelio.nonprofit.email.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "config")
public class ConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "template", nullable = false)
    private String template;

}

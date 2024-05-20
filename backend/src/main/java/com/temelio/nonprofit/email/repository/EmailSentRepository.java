package com.temelio.nonprofit.email.repository;

import com.temelio.nonprofit.email.model.EmailSentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailSentRepository extends CrudRepository<EmailSentEntity, UUID> {
}

package com.temelio.nonprofit.email.repository;

import com.temelio.nonprofit.email.model.DonationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DonationRepository extends CrudRepository<DonationEntity, UUID> {

    List<DonationEntity> findByIsDonationNotifiedFalse();

}

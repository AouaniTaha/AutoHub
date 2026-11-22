package com.autohub.garage.repository;

import com.autohub.garage.domain.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MechanicRepository extends JpaRepository<Mechanic, UUID> {

    List<Mechanic> findByGarageId(UUID garageId);
}

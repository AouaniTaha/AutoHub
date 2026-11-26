package tn.esprit.autoloc.garage.repository;

import tn.esprit.autoloc.garage.domain.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MechanicRepository extends JpaRepository<Mechanic, UUID> {

    List<Mechanic> findByGarageId(UUID garageId);
}

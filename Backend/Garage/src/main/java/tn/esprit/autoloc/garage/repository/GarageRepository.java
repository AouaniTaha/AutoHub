package tn.esprit.autoloc.garage.repository;

import tn.esprit.autoloc.garage.domain.Garage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GarageRepository extends JpaRepository<Garage, UUID> {

    List<Garage> findByVilleIgnoreCase(String ville);
}

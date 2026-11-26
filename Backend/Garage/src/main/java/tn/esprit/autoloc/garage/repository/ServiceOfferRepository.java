package tn.esprit.autoloc.garage.repository;

import tn.esprit.autoloc.garage.domain.ServiceOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceOfferRepository extends JpaRepository<ServiceOffer, UUID> {

    List<ServiceOffer> findByGarageId(UUID garageId);

    Optional<ServiceOffer> findByGarageIdAndServiceTypeCode(UUID garageId, String serviceTypeCode);
}

package tn.esprit.autoloc.garage.web;

import tn.esprit.autoloc.garage.domain.Garage;
import tn.esprit.autoloc.garage.domain.Mechanic;
import tn.esprit.autoloc.garage.domain.ServiceOffer;
import tn.esprit.autoloc.garage.repository.GarageRepository;
import tn.esprit.autoloc.garage.repository.MechanicRepository;
import tn.esprit.autoloc.garage.repository.ServiceOfferRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class GarageController {

    private final GarageRepository garageRepository;
    private final ServiceOfferRepository serviceOfferRepository;
    private final MechanicRepository mechanicRepository;

    public GarageController(GarageRepository garageRepository,
                            ServiceOfferRepository serviceOfferRepository,
                            MechanicRepository mechanicRepository) {
        this.garageRepository = garageRepository;
        this.serviceOfferRepository = serviceOfferRepository;
        this.mechanicRepository = mechanicRepository;
    }

    @GetMapping("/garages")
    public List<Garage> search(@RequestParam(required = false) String ville) {
        return (ville == null || ville.isBlank())
                ? garageRepository.findAll()
                : garageRepository.findByVilleIgnoreCase(ville);
    }

    @GetMapping("/garages/{id}")
    public ResponseEntity<Garage> getById(@PathVariable UUID id) {
        return garageRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/garages")
    public ResponseEntity<Garage> create(@RequestBody Garage garage) {
        Garage saved = garageRepository.save(garage);
        return ResponseEntity.created(URI.create("/api/garages/" + saved.getId())).body(saved);
    }

    @PutMapping("/garages/{id}")
    public ResponseEntity<Garage> update(@PathVariable UUID id, @RequestBody Garage garage) {
        return garageRepository.findById(id).map(existing -> {
            existing.setNom(garage.getNom());
            existing.setVille(garage.getVille());
            existing.setAdresse(garage.getAdresse());
            existing.setMarques(garage.getMarques());
            return ResponseEntity.ok(garageRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/garages/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!garageRepository.existsById(id)) return ResponseEntity.notFound().build();
        garageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/garages/{id}/services")
    public List<ServiceOffer> listServices(@PathVariable UUID id) {
        return serviceOfferRepository.findByGarageId(id);
    }

    @PostMapping("/garages/{id}/services")
    public ResponseEntity<ServiceOffer> addService(@PathVariable UUID id, @RequestBody ServiceOffer offer) {
        return garageRepository.findById(id).map(g -> {
            if (serviceOfferRepository.findByGarageIdAndServiceTypeCode(id, offer.getServiceTypeCode()).isPresent()) {
                return ResponseEntity.status(409).<ServiceOffer>build();
            }
            offer.setGarage(g);
            return ResponseEntity.created(URI.create("/api/services/" + offer.getId()))
                    .body(serviceOfferRepository.save(offer));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<ServiceOffer> updateService(@PathVariable UUID id, @RequestBody ServiceOffer offer) {
        return serviceOfferRepository.findById(id).map(existing -> {
            existing.setNom(offer.getNom());
            existing.setPrix(offer.getPrix());
            existing.setDureeMin(offer.getDureeMin());
            existing.setActive(offer.getActive());
            return ResponseEntity.ok(serviceOfferRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
        if (!serviceOfferRepository.existsById(id)) return ResponseEntity.notFound().build();
        serviceOfferRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/garages/{id}/mechanics")
    public List<Mechanic> listMechanics(@PathVariable UUID id) {
        return mechanicRepository.findByGarageId(id);
    }

    @PostMapping("/garages/{id}/mechanics")
    public ResponseEntity<Mechanic> addMechanic(@PathVariable UUID id, @RequestBody Mechanic mechanic) {
        return garageRepository.findById(id).map(g -> {
            mechanic.setGarage(g);
            return ResponseEntity.created(URI.create("/api/mechanics/" + mechanic.getId()))
                    .body(mechanicRepository.save(mechanic));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/mechanics/{id}")
    public ResponseEntity<Mechanic> updateMechanic(@PathVariable UUID id, @RequestBody Mechanic mechanic) {
        return mechanicRepository.findById(id).map(existing -> {
            existing.setNom(mechanic.getNom());
            existing.setDisponible(mechanic.getDisponible());
            return ResponseEntity.ok(mechanicRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/mechanics/{id}")
    public ResponseEntity<Void> deleteMechanic(@PathVariable UUID id) {
        if (!mechanicRepository.existsById(id)) return ResponseEntity.notFound().build();
        mechanicRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mechanics/{id}/qualified")
    public ResponseEntity<Boolean> isMechanicQualified(@PathVariable UUID id,
                                                       @RequestParam String serviceTypeCode) {
        return mechanicRepository.findById(id)
                .map(m -> ResponseEntity.ok(m.isQualifiedFor(serviceTypeCode)))
                .orElse(ResponseEntity.notFound().build());
    }
}

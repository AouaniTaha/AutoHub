package com.autohub.garage.web;

import com.autohub.garage.domain.Garage;
import com.autohub.garage.domain.Mechanic;
import com.autohub.garage.domain.ServiceOffer;
import com.autohub.garage.repository.GarageRepository;
import com.autohub.garage.repository.MechanicRepository;
import com.autohub.garage.repository.ServiceOfferRepository;
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

    @GetMapping("/mechanics/{id}/qualified")
    public ResponseEntity<Boolean> isMechanicQualified(@PathVariable UUID id,
                                                       @RequestParam String serviceTypeCode) {
        return mechanicRepository.findById(id)
                .map(m -> ResponseEntity.ok(m.isQualifiedFor(serviceTypeCode)))
                .orElse(ResponseEntity.notFound().build());
    }
}

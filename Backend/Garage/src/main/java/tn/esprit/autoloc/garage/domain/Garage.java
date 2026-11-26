package tn.esprit.autoloc.garage.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "garages")
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    private String nom;

    private String ville;

    private String adresse;

    @ElementCollection
    @CollectionTable(name = "garage_marques", joinColumns = @JoinColumn(name = "garage_id"))
    @Column(name = "marque")
    private List<String> marques = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceOffer> services = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpeningHours> openingHours = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mechanic> mechanics = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getOwnerId() { return ownerId; }
    public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public List<String> getMarques() { return marques; }
    public void setMarques(List<String> marques) { this.marques = marques; }

    public List<ServiceOffer> getServices() { return services; }
    public void setServices(List<ServiceOffer> services) { this.services = services; }

    public List<OpeningHours> getOpeningHours() { return openingHours; }
    public void setOpeningHours(List<OpeningHours> openingHours) { this.openingHours = openingHours; }

    public List<Mechanic> getMechanics() { return mechanics; }
    public void setMechanics(List<Mechanic> mechanics) { this.mechanics = mechanics; }
}

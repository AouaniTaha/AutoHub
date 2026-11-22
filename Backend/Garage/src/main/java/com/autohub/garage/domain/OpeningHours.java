package com.autohub.garage.domain;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "opening_hours",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_garage_day",
                columnNames = {"garage_id", "jour"}))
public class OpeningHours {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private DayOfWeek jour;

    @Column(nullable = false)
    private LocalTime ouverture;

    @Column(nullable = false)
    private LocalTime fermeture;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Garage getGarage() { return garage; }
    public void setGarage(Garage garage) { this.garage = garage; }

    public DayOfWeek getJour() { return jour; }
    public void setJour(DayOfWeek jour) { this.jour = jour; }

    public LocalTime getOuverture() { return ouverture; }
    public void setOuverture(LocalTime ouverture) { this.ouverture = ouverture; }

    public LocalTime getFermeture() { return fermeture; }
    public void setFermeture(LocalTime fermeture) { this.fermeture = fermeture; }
}

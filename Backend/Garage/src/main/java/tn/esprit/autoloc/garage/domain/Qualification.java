package tn.esprit.autoloc.garage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "qualifications",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_mechanic_service_type",
                columnNames = {"mechanic_id", "service_type_code"}))
public class Qualification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "mechanic_id", nullable = false)
    private Mechanic mechanic;

    @Column(name = "service_type_code", nullable = false, length = 64)
    private String serviceTypeCode;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Mechanic getMechanic() { return mechanic; }
    public void setMechanic(Mechanic mechanic) { this.mechanic = mechanic; }

    public String getServiceTypeCode() { return serviceTypeCode; }
    public void setServiceTypeCode(String serviceTypeCode) { this.serviceTypeCode = serviceTypeCode; }
}

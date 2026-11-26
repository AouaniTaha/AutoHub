package tn.esprit.autoloc.order.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @NotNull(message = "listingId est obligatoire")
    private Long listingId;

    @NotBlank(message = "titleSnapshot est obligatoire")
    private String titleSnapshot;

    @NotNull(message = "sellerId est obligatoire")
    private UUID sellerId;

    @NotNull(message = "unitPrice est obligatoire")
    @DecimalMin(value = "0.01", message = "unitPrice doit etre strictement positif")
    private BigDecimal unitPrice;

    @Min(value = 1, message = "quantity doit etre au moins 1")
    private int quantity;

    private String reservationId;
}

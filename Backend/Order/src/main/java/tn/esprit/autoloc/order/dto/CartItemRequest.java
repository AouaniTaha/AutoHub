package tn.esprit.autoloc.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {

    @NotNull(message = "listingId est obligatoire")
    private Long listingId;

    @Min(value = 1, message = "quantity doit etre au moins 1")
    private int quantity;
}

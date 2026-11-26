package tn.esprit.autoloc.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "buyerId est obligatoire")
    private UUID buyerId;

    @NotEmpty(message = "Une commande doit contenir au moins un article")
    @Valid
    private List<OrderItemRequest> items;
}

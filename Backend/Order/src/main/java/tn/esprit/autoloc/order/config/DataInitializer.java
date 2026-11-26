package tn.esprit.autoloc.order.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.order.dto.CartItemRequest;
import tn.esprit.autoloc.order.dto.CreateOrderRequest;
import tn.esprit.autoloc.order.dto.OrderItemRequest;
import tn.esprit.autoloc.order.domain.enums.OrderStatus;
import tn.esprit.autoloc.order.repository.CartRepository;
import tn.esprit.autoloc.order.repository.OrderRepository;
import tn.esprit.autoloc.order.service.CartService;
import tn.esprit.autoloc.order.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderService orderService;
    private final CartService cartService;

    @Override
    public void run(String... args) {
        UUID sampleBuyerId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID sampleSellerId = UUID.fromString("22222222-2222-2222-2222-222222222222");

        if (orderRepository.count() == 0) {
            CreateOrderRequest sampleOrder = new CreateOrderRequest(
                    sampleBuyerId,
                    List.of(
                            new OrderItemRequest(101L, "Plaquettes de frein Bosch Avant", sampleSellerId, new BigDecimal("145.00"), 2, "RES-001"),
                            new OrderItemRequest(102L, "Filtre a huile Mann-Filter", sampleSellerId, new BigDecimal("35.50"), 1, "RES-002")
                    )
            );
            var created = orderService.createOrder(sampleOrder);
            orderService.updateOrderStatus(created.getId(), OrderStatus.CONFIRMED);
        }

        if (cartRepository.count() == 0) {
            cartService.addItemToCart(sampleBuyerId, new CartItemRequest(205L, 2));
            cartService.addItemToCart(sampleBuyerId, new CartItemRequest(310L, 1));
        }
    }
}

package tn.esprit.autoloc.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.order.dto.CreateOrderRequest;
import tn.esprit.autoloc.order.dto.OrderItemRequest;
import tn.esprit.autoloc.order.domain.entity.Order;
import tn.esprit.autoloc.order.domain.enums.OrderStatus;
import tn.esprit.autoloc.order.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"eureka.client.enabled=false"})
class OrderApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
    }

    @Test
    void createAndUpdateOrderStatus() {
        CreateOrderRequest req = new CreateOrderRequest(
                UUID.randomUUID(),
                List.of(new OrderItemRequest(1L, "Batterie 12V Varta", UUID.randomUUID(), new BigDecimal("250.00"), 2, "RES-TEST"))
        );

        Order created = orderService.createOrder(req);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getTotalAmount()).isEqualByComparingTo("500.00");
        assertThat(created.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(created.getStatusHistory()).hasSize(1);

        Order updated = orderService.updateOrderStatus(created.getId(), OrderStatus.PAID);
        assertThat(updated.getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(updated.getStatusHistory()).hasSize(2);
    }
}

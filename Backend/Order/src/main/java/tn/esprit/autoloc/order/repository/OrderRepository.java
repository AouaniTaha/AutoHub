package tn.esprit.autoloc.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.order.domain.entity.Order;
import tn.esprit.autoloc.order.domain.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerId(UUID buyerId);

    List<Order> findByStatus(OrderStatus status);
}

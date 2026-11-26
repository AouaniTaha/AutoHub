package tn.esprit.autoloc.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.order.dto.CreateOrderRequest;
import tn.esprit.autoloc.order.dto.OrderItemRequest;
import tn.esprit.autoloc.order.domain.entity.Order;
import tn.esprit.autoloc.order.domain.entity.OrderItem;
import tn.esprit.autoloc.order.domain.enums.OrderStatus;
import tn.esprit.autoloc.order.exception.ResourceNotFoundException;
import tn.esprit.autoloc.order.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(CreateOrderRequest request) {
        Order order = Order.builder()
                .buyerId(request.getBuyerId())
                .createdAt(LocalDateTime.now())
                .build();

        for (OrderItemRequest itemReq : request.getItems()) {
            OrderItem item = OrderItem.builder()
                    .listingId(itemReq.getListingId())
                    .titleSnapshot(itemReq.getTitleSnapshot())
                    .sellerId(itemReq.getSellerId())
                    .unitPrice(itemReq.getUnitPrice())
                    .quantity(itemReq.getQuantity())
                    .reservationId(itemReq.getReservationId())
                    .build();
            order.addItem(item);
        }

        order.addStatusHistory(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable avec l'id : " + id));
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByBuyerId(UUID buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Order updateOrder(Long id, CreateOrderRequest request) {
        Order order = getOrderById(id);
        order.setBuyerId(request.getBuyerId());
        order.getItems().clear();

        for (OrderItemRequest itemReq : request.getItems()) {
            OrderItem item = OrderItem.builder()
                    .listingId(itemReq.getListingId())
                    .titleSnapshot(itemReq.getTitleSnapshot())
                    .sellerId(itemReq.getSellerId())
                    .unitPrice(itemReq.getUnitPrice())
                    .quantity(itemReq.getQuantity())
                    .reservationId(itemReq.getReservationId())
                    .build();
            order.addItem(item);
        }

        return orderRepository.save(order);
    }

    public Order updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = getOrderById(id);
        order.addStatusHistory(newStatus);
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }
}

package tn.esprit.autoloc.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.order.dto.CartItemRequest;
import tn.esprit.autoloc.order.domain.entity.Cart;
import tn.esprit.autoloc.order.service.CartService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCarts() {
        return ResponseEntity.ok(cartService.getAllCarts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<Cart> getOrCreateCart(@PathVariable UUID buyerId) {
        return ResponseEntity.ok(cartService.getOrCreateCartByBuyerId(buyerId));
    }

    @PostMapping("/buyer/{buyerId}/items")
    public ResponseEntity<Cart> addItem(@PathVariable UUID buyerId,
                                        @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToCart(buyerId, request));
    }

    @PutMapping("/buyer/{buyerId}/items/{itemId}")
    public ResponseEntity<Cart> updateItemQuantity(@PathVariable UUID buyerId,
                                                   @PathVariable Long itemId,
                                                   @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(buyerId, itemId, quantity));
    }

    @DeleteMapping("/buyer/{buyerId}/items/{itemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable UUID buyerId,
                                           @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(buyerId, itemId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}

package tn.esprit.autoloc.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.order.dto.CartItemRequest;
import tn.esprit.autoloc.order.domain.entity.Cart;
import tn.esprit.autoloc.order.domain.entity.CartItem;
import tn.esprit.autoloc.order.exception.ResourceNotFoundException;
import tn.esprit.autoloc.order.repository.CartRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Panier introuvable avec l'id : " + id));
    }

    public Cart getOrCreateCartByBuyerId(UUID buyerId) {
        return cartRepository.findByBuyerId(buyerId)
                .orElseGet(() -> cartRepository.save(Cart.builder().buyerId(buyerId).build()));
    }

    public Cart addItemToCart(UUID buyerId, CartItemRequest request) {
        Cart cart = getOrCreateCartByBuyerId(buyerId);

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getListingId().equals(request.getListingId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .listingId(request.getListingId())
                    .quantity(request.getQuantity())
                    .build();
            cart.addItem(newItem);
        }

        return cartRepository.save(cart);
    }

    public Cart updateCartItemQuantity(UUID buyerId, Long itemId, int quantity) {
        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Panier introuvable pour l'acheteur : " + buyerId));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable dans le panier avec l'id : " + itemId));

        item.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(UUID buyerId, Long itemId) {
        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Panier introuvable pour l'acheteur : " + buyerId));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable dans le panier avec l'id : " + itemId));

        cart.removeItem(item);
        return cartRepository.save(cart);
    }

    public void deleteCart(Long id) {
        Cart cart = getCartById(id);
        cartRepository.delete(cart);
    }
}

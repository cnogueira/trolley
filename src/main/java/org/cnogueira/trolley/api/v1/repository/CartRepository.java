package org.cnogueira.trolley.api.v1.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.cnogueira.trolley.api.v1.dto.Cart;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository {

    private final Map<UUID, Cart> carts = new HashMap<>();

    public void addCart(final Cart cart) {
        carts.put(cart.getId(), cart);
    }

    public Optional<Cart> getById(final UUID cartId) {
        return Optional.ofNullable(carts.get(cartId));
    }
}

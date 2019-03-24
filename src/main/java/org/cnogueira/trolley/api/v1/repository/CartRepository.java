package org.cnogueira.trolley.api.v1.repository;

import lombok.RequiredArgsConstructor;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final CartFactory cartFactory;
    private final Map<UUID, Cart> carts = new HashMap<>();

    public void addCart(final Cart cart) {
        carts.put(cart.getId(), cartFactory.cloneOf(cart));
    }

    public Optional<Cart> getById(final UUID cartId) {
        return Optional.ofNullable(carts.get(cartId))
            .map(cartFactory::cloneOf);
    }

    public void replaceCart(final Cart cart) {
        addCart(cart);
    }

    public void stateChanged(final Cart cart) {
        replaceCart(cart);
    }

}

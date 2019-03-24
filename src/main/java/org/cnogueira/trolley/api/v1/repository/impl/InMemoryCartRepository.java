package org.cnogueira.trolley.api.v1.repository.impl;

import lombok.RequiredArgsConstructor;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.cnogueira.trolley.api.v1.repository.CartRepository;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObservable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class InMemoryCartRepository implements CartRepository {

    private final CartFactory cartFactory;
    private final Map<UUID, Cart> carts = new HashMap<>();

    @Override
    public void addCart(final Cart cart) {
        carts.put(cart.getId(), cartFactory.cloneOf(cart));
    }

    @Override
    public Optional<Cart> getById(final UUID cartId) {
        return Optional.ofNullable(carts.get(cartId))
            .map(cartFactory::cloneOf);
    }

    @Override
    public void replaceCart(final Cart cart) {
        addCart(cart);
    }

    @Override
    public void onStateChanged(final StateChangeObservable emitter) {
        if (!(emitter instanceof Cart)) {
            throw new IllegalArgumentException("CartRepository must be subscribed to Cart state changes only");
        }

        replaceCart((Cart) emitter);
    }
}

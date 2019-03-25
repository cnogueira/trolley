package org.cnogueira.trolley.api.v1.repository;

import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObservable;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObserver;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends StateChangeObserver {
    void addCart(Cart cart);

    Optional<Cart> getById(UUID cartId);

    default void replaceCart(final Cart cart) {
        addCart(cart);
    }

    @Override
    default void onStateChanged(final StateChangeObservable emitter) {
        if (!(emitter instanceof Cart)) {
            throw new IllegalArgumentException("CartRepository must be subscribed to Cart state changes only");
        }

        replaceCart((Cart) emitter);
    }
}

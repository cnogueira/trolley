package org.cnogueira.trolley.api.v1.repository;

import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObservable;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObserver;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends StateChangeObserver {
    void addCart(Cart cart);

    Optional<Cart> getById(UUID cartId);

    void replaceCart(Cart cart);

    @Override
    void onStateChanged(StateChangeObservable emitter);
}

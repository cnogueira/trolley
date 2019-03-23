package org.cnogueira.trolley.api.v1.repository;

import java.util.Optional;
import java.util.UUID;
import org.cnogueira.trolley.api.v1.dto.Cart;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository {
    public void addCart(final Cart cart) {
    }

    public Optional<Cart> getById(UUID eq) {
        return null;
    }
}

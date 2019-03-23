package org.cnogueira.trolley.api.v1.domain.factory.impl;

import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultCartFactory implements CartFactory {

    @Override
    public Cart from(final CartCreateRequest cartCreateRequest) {
        return Cart.builder()
            .id(UUID.randomUUID())
            .name(cartCreateRequest.getName())
            .build();
    }

    @Override
    public Cart withName(final String cartName) {
        return Cart.builder()
            .id(UUID.randomUUID())
            .name(cartName)
            .build();
    }
}

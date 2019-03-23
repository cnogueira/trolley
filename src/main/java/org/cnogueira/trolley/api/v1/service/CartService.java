package org.cnogueira.trolley.api.v1.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.exceptions.CartNotFoundException;
import org.cnogueira.trolley.api.v1.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart createCart(final CartCreateRequest cartCreateRequest) {
        val cart = Cart.from(cartCreateRequest);

        cartRepository.addCart(cart);

        return cart;
    }

    public Cart getCart(final UUID cartId) {
        return cartRepository.getById(cartId)
                .orElseThrow(CartNotFoundException::new);
    }
}

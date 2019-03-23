package org.cnogueira.trolley.api.v1.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.cnogueira.trolley.api.v1.dto.Cart;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
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
}

package org.cnogueira.trolley.api.v1.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.Item;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.dto.ItemAddRequest;
import org.cnogueira.trolley.api.v1.exceptions.CartNotFoundException;
import org.cnogueira.trolley.api.v1.repository.CartRepository;
import org.cnogueira.trolley.api.v1.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public Cart createCart(final CartCreateRequest cartCreateRequest) {
        val cart = Cart.from(cartCreateRequest);

        cartRepository.addCart(cart);

        return cart;
    }

    public Cart getCart(final UUID cartId) {
        return cartRepository.getById(cartId)
                .orElseThrow(CartNotFoundException::new);
    }

    public Item addItem(final UUID cartId, final ItemAddRequest itemAddRequest) {
        return null;
    }
}

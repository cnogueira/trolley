package org.cnogueira.trolley.api.v1.domain.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.val;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.Item;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeNotifierFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CartFactory {

    private final StateChangeNotifierFactory stateChangeNotifierFactory;
    private final ObjectMapper objectMapper;

    public Cart from(final CartCreateRequest cartCreateRequest) {
        return with(cartCreateRequest.getName());
    }

    public Cart with(final String cartName) {
        return with(nextCartId(), cartName);
    }


    public Cart with(final String cartName, final List<Item> itemList) {
        return with(nextCartId(), cartName, itemList);
    }

    public Cart with(final UUID cartId, final String cartName) {
        return with(cartId, cartName, Collections.emptyList());
    }

    public Cart with(final UUID cartId, final String cartName, final List<Item> itemList) {
        val cart = new Cart(stateChangeNotifierFactory.createStateChangeNotifier(), cartId, cartName);

        itemList.forEach(cart::addItem);

        return cart;
    }

    public Cart cloneOf(final Cart cart) {
        return with(cart.getId(), cart.getName(), cart.getItems());
    }

    private UUID nextCartId() {
        return UUID.randomUUID();
    }

    public static CartFactory create() {
        return new CartFactory(new StateChangeNotifierFactory(), new ObjectMapper());
    }

    public Cart fromJson(final String originalCartAsJson) throws IOException {
        val deserializedCart = objectMapper.readValue(originalCartAsJson, Cart.class);

        return cloneOf(deserializedCart);
    }
}

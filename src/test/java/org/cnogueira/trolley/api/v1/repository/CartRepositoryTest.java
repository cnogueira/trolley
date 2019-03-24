package org.cnogueira.trolley.api.v1.repository;


import lombok.val;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.Item;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CartRepositoryTest {

    private CartRepository cartRepository;
    private CartFactory cartFactory = CartFactory.create();

    @Before
    public void setUp() {
        cartRepository = new CartRepository();
    }

    @Test
    public void emptyByDefault() {
        // when
        val optionalCart = cartRepository.getById(UUID.randomUUID());

        //then
        assertThat(optionalCart).isEmpty();
    }

    @Test
    public void allowsGetByIdAfterAddingIt() {
        // given
        val cart = cartFactory.with("some random name");

        // when
        cartRepository.addCart(cart);
        val optionalCart = cartRepository.getById(cart.getId());

        //then
        assertThat(optionalCart).contains(cart);
    }

    @Test
    public void replaceCart_replacesCartById() {
        // given
        val cart = cartFactory.with("some random name");
        val cart2 = cartFactory.with(cart.getId(), "another name", singletonList(Item.withName("just an item")));
        cartRepository.addCart(cart);

        // when
        cartRepository.replaceCart(cart2);

        // then
        val updatedCart = getByIdFromRepositoryOrFail(cart.getId());
        assertThat(updatedCart).isEqualTo(cart2);
    }

    private Cart getByIdFromRepositoryOrFail(UUID cartId) {
        val optionalCart = cartRepository.getById(cartId);

        if (!optionalCart.isPresent()) {
            fail("Expected CartRepository to contain a Cart with id: " + cartId);
        }

        return optionalCart.get();
    }
}

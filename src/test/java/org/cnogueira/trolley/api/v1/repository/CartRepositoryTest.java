package org.cnogueira.trolley.api.v1.repository;


import lombok.val;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.Item;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CartRepositoryTest {

    private CartRepository cartRepository;
    private CartFactory cartFactory;

    @Before
    public void setUp() {
        cartFactory = CartFactory.create();
        cartRepository = new CartRepository(cartFactory);
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

    @Test
    public void stateChanged_updatesCart() {
        // given
        val cart = cartFactory.with("test cart");
        assertThat(cartRepository.getById(cart.getId())).isEmpty();

        // when
        cartRepository.stateChanged(cart);

        // then
        assertThat(cartRepository.getById(cart.getId())).contains(cart);
    }

    /**
     * Necessary to prevent unwanted item "updates" (as otherwise business logic will share same instance)
     */
    @Test
    public void getById_returnsClonedInstance() {
        // given
        val originalCart = cartFactory.with("test cart", asList(Item.withName("A"), Item.withName("B")));
        cartRepository.addCart(originalCart);

        // when
        val cart = getByIdFromRepositoryOrFail(originalCart.getId());

        //then
        assertThat(cart).isEqualTo(originalCart);
        assertThat(cart).isNotSameAs(originalCart);
    }

    private Cart getByIdFromRepositoryOrFail(UUID cartId) {
        val optionalCart = cartRepository.getById(cartId);

        if (!optionalCart.isPresent()) {
            fail("Expected CartRepository to contain a Cart with id: " + cartId);
        }

        return optionalCart.get();
    }
}

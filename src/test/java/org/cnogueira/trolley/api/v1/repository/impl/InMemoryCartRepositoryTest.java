package org.cnogueira.trolley.api.v1.repository.impl;


import lombok.val;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.Item;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObservable;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;

public class InMemoryCartRepositoryTest {

    private InMemoryCartRepository inMemoryCartRepository;
    private CartFactory cartFactory;

    @Before
    public void setUp() {
        cartFactory = CartFactory.create();
        inMemoryCartRepository = new InMemoryCartRepository(cartFactory);
    }

    @Test
    public void emptyByDefault() {
        // when
        val optionalCart = inMemoryCartRepository.getById(UUID.randomUUID());

        //then
        assertThat(optionalCart).isEmpty();
    }

    @Test
    public void allowsGetByIdAfterAddingIt() {
        // given
        val cart = cartFactory.with("some random name");

        // when
        inMemoryCartRepository.addCart(cart);
        val optionalCart = inMemoryCartRepository.getById(cart.getId());

        //then
        assertThat(optionalCart).contains(cart);
    }

    @Test
    public void replaceCart_replacesCartById() {
        // given
        val cart = cartFactory.with("some random name");
        val cart2 = cartFactory.with(cart.getId(), "another name", singletonList(Item.withName("just an item")));
        inMemoryCartRepository.addCart(cart);

        // when
        inMemoryCartRepository.replaceCart(cart2);

        // then
        val updatedCart = getByIdFromRepositoryOrFail(cart.getId());
        assertThat(updatedCart).isEqualTo(cart2);
    }

    @Test
    public void onStateChanged_updatesCart() {
        // given
        val cart = cartFactory.with("test cart");
        assertThat(inMemoryCartRepository.getById(cart.getId())).isEmpty();

        // when
        inMemoryCartRepository.onStateChanged(cart);

        // then
        assertThat(inMemoryCartRepository.getById(cart.getId())).contains(cart);
    }

    @Test(expected = IllegalArgumentException.class)
    public void onStateChanged_throwsIfCalledWithAnUnknownEmitter() {
        // given
        val unknownEmitter = mock(StateChangeObservable.class);

        // when
        inMemoryCartRepository.onStateChanged(unknownEmitter);

        // then
        fail("CartRepository must throw when notified on state changed of something that's not a Cart");
    }

    /**
     * Necessary to prevent unwanted item "updates" (as otherwise business logic will share same instance)
     */
    @Test
    public void getById_returnsClonedInstance() {
        // given
        val originalCart = cartFactory.with("test cart", asList(Item.withName("A"), Item.withName("B")));
        inMemoryCartRepository.addCart(originalCart);

        // when
        val cart = getByIdFromRepositoryOrFail(originalCart.getId());

        //then
        assertThat(cart).isEqualTo(originalCart);
        assertThat(cart).isNotSameAs(originalCart);
    }

    private Cart getByIdFromRepositoryOrFail(UUID cartId) {
        val optionalCart = inMemoryCartRepository.getById(cartId);

        if (!optionalCart.isPresent()) {
            fail("Expected CartRepository to contain a Cart with id: " + cartId);
        }

        return optionalCart.get();
    }
}

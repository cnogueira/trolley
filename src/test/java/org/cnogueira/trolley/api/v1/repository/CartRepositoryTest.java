package org.cnogueira.trolley.api.v1.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import lombok.val;
import org.cnogueira.trolley.api.v1.dto.Cart;
import org.junit.Before;
import org.junit.Test;

public class CartRepositoryTest {

    private CartRepository cartRepository;

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
        val cart = Cart.builder()
                .id(UUID.randomUUID())
                .name("some random name")
                .build();

        // when
        cartRepository.addCart(cart);
        val optionalCart = cartRepository.getById(cart.getId());

        //then
        assertThat(optionalCart).contains(cart);
    }
}

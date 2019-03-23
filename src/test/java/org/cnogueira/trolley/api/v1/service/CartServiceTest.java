package org.cnogueira.trolley.api.v1.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.UUID;
import lombok.val;
import org.cnogueira.trolley.api.v1.dto.Cart;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.exceptions.CartNotFoundException;
import org.cnogueira.trolley.api.v1.repository.CartRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    private CartService cartService;

    @Before
    public void setUp() {
        cartService = new CartService(cartRepository);
    }

    @Test
    public void createCart_createsCart_and_AddsItToRepository() {
        // given
        val cartCreateRequest = CartCreateRequest.withName("some name");

        // when
        val createdCart = cartService.createCart(cartCreateRequest);

        //then
        assertThat(createdCart).isNotNull();
        assertThat(createdCart.getId()).isNotNull();
        assertThat(createdCart.getName()).isEqualTo(cartCreateRequest.getName());
        verify(cartRepository, times(1)).addCart(same(createdCart));
    }

    @Test
    public void getCart_delegatesToRepository() {
        // given
        val cart = Cart.builder()
                .id(UUID.randomUUID())
                .name("some other name")
                .build();
        given(cartRepository.getById(eq(cart.getId()))).willReturn(Optional.of(cart));

        // when
        val receivedCart = cartService.getCart(cart.getId());

        //then
        assertThat(receivedCart).isSameAs(cart);
        verify(cartRepository, times(1)).getById(eq(cart.getId()));
    }

    @Test(expected = CartNotFoundException.class)
    public void getCart_throwsCartNotFoundExceptionWhenRepositoryReturnsEmpty() {
        // given
        given(cartRepository.getById(any())).willReturn(Optional.empty());

        // when
        cartService.getCart(UUID.randomUUID());
    }
}

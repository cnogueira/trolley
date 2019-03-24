package org.cnogueira.trolley.api.v1.service;

import lombok.val;
import org.cnogueira.trolley.api.v1.domain.Item;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.dto.ItemAddRequest;
import org.cnogueira.trolley.api.v1.exceptions.CartNotFoundException;
import org.cnogueira.trolley.api.v1.repository.CartRepository;
import org.cnogueira.trolley.api.v1.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CartFactory cartFactoryMock;

    private CartFactory cartFactory = CartFactory.create();

    private CartService cartService;

    @Before
    public void setUp() {
        cartService = new CartService(cartRepository, itemRepository, cartFactoryMock);
    }

    @Test
    public void createCart_createsCart_and_AddsItToRepository() {
        // given
        val cartCreateRequest = CartCreateRequest.withName("some name");
        val cart = spy(cartFactory.with("some name"));
        given(cartFactoryMock.from(same(cartCreateRequest))).willReturn(cart);

        // when
        val createdCart = cartService.createCart(cartCreateRequest);

        //then
        assertThat(createdCart).isNotNull();
        assertThat(createdCart.getId()).isNotNull();
        assertThat(createdCart.getName()).isEqualTo(cartCreateRequest.getName());
        verify(cartRepository, times(1)).addCart(same(createdCart));
        verify(cart, times(1)).addStateChangeObserver(same(cartRepository));
    }

    @Test
    public void getCart_delegatesToRepository_and_SubscribesRepository() {
        // given
        val cart = spy(cartFactory.with("some other name"));
        given(cartRepository.getById(eq(cart.getId()))).willReturn(Optional.of(cart));

        // when
        val receivedCart = cartService.getCart(cart.getId());

        //then
        assertThat(receivedCart).isSameAs(cart);
        verify(cartRepository, times(1)).getById(eq(cart.getId()));
        verify(cart, times(1)).addStateChangeObserver(same(cartRepository));
    }

    @Test(expected = CartNotFoundException.class)
    public void getCart_throwsCartNotFoundExceptionWhenRepositoryReturnsEmpty() {
        // given
        given(cartRepository.getById(any())).willReturn(Optional.empty());

        // when
        cartService.getCart(UUID.randomUUID());

        // then
        fail("Should throw exception when there's no cart with the provided cartId");
    }

    @Test
    public void addItem_fetchesCart_createsItem_and_updatesCartRepository() {
        // given
        val cart = spy(cartFactory.with("cart 1", singletonList(Item.withName("item 1"))));
        val itemAddRequest = ItemAddRequest.withName("item 2");
        given(cartRepository.getById(eq(cart.getId()))).willReturn(Optional.of(cart));

        // when
        val addedItem = cartService.addItem(cart.getId(), itemAddRequest);

        // then
        assertThat(addedItem).isNotNull();
        assertThat(addedItem.getName()).isEqualTo(itemAddRequest.getName());

        verify(itemRepository, times(1)).addItem(same(addedItem));

        InOrder orderVerifier = Mockito.inOrder(cart);

        orderVerifier.verify(cart).addStateChangeObserver(same(cartRepository));
        orderVerifier.verify(cart).addItem(argThat(item -> itemAddRequest.getName().equals(item.getName())));
        orderVerifier.verifyNoMoreInteractions();
    }

    @Test(expected = CartNotFoundException.class)
    public void addItem_throwsCartNotFoundExceptionWhenRepositoryReturnsEmpty() {
        // given
        given(cartRepository.getById(any())).willReturn(Optional.empty());

        // when
        cartService.addItem(UUID.randomUUID(), ItemAddRequest.withName("not relevant"));

        // then
        fail("Should throw exception when there's no cart with the provided cartId");
    }
}

package org.cnogueira.trolley.api.v1.domain;

import lombok.val;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeNotifierFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CartTest {

    @Spy
    private StateChangeNotifierFactory stateChangeNotifierFactory;

    private CartFactory cartFactory;

    @Before
    public void setUp() {
        cartFactory = new CartFactory(stateChangeNotifierFactory);
    }

    @Test
    public void from_CartGetsCreatedWithZeroItems() {
        // when
        val cart = cartFactory.from(CartCreateRequest.withName("abc"));

        // then
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    public void withName_CartGetsCreatedWithZeroItems() {
        // when
        val cart = cartFactory.with("cart 1");

        // then
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    public void addItem() {
        // given
        val cart = cartFactory.with("some name");
        val item = Item.withName("item 1");

        // when
        cart.addItem(item);

        // then
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems()).contains(item);
    }

    @Test
    public void addItem_assertAddItemIsOnlyWayOfModifyingCartItems() {
        // given
        val cart = cartFactory.with("some name");
        val item = Item.withName("item 1");
        cart.addItem(item);
        val items = cart.getItems();

        // when
        try {
            items.add(Item.withName("item 2"));
        } catch (Exception ignored) {
            // it is ok to throw, as it might be an unmodifiable collection
        }

        // then
        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems()).contains(item);
    }
}

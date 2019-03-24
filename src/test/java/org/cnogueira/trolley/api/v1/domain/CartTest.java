package org.cnogueira.trolley.api.v1.domain;

import lombok.val;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeNotifier;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeNotifierFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    public void addItem_notifiesStateChangeObservers() {
        // given
        val stateChangeNotifier = mock(StateChangeNotifier.class);
        given(stateChangeNotifierFactory.createStateChangeNotifier()).willReturn(stateChangeNotifier);
        val cart = cartFactory.with("cart with StateChangeNotifier spy");
        val item = Item.withName("just an item");

        // then
        cart.addItem(item);

        // when
        verify(stateChangeNotifier, times(1)).notifyStateChanged(same(cart));
    }
}

package org.cnogueira.trolley.api.v1.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeNotifier;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeNotifierFactory;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    @Test
    public void subscribeStateChangeObserver_registersObserverToNotifier() {
        // given
        val stateChangeNotifier = mock(StateChangeNotifier.class);
        given(stateChangeNotifierFactory.createStateChangeNotifier()).willReturn(stateChangeNotifier);
        val cart = cartFactory.with("cart with StateChangeNotifier spy");
        val observer = mock(StateChangeObserver.class);

        // then
        cart.subscribeStateChangeObserver(observer);

        // when
        verify(stateChangeNotifier, times(1)).subscribe(same(observer));
    }

    @Test
    public void unsubscribeStateChangeObserver_unregistersObserverFromNotifier() {
        // given
        val stateChangeNotifier = mock(StateChangeNotifier.class);
        given(stateChangeNotifierFactory.createStateChangeNotifier()).willReturn(stateChangeNotifier);
        val cart = cartFactory.with("cart with StateChangeNotifier spy");
        val observer = mock(StateChangeObserver.class);

        // then
        cart.unsubscribeStateChangeObserver(observer);

        // when
        verify(stateChangeNotifier, times(1)).unsubscribe(same(observer));
    }

    @Test
    public void serialize_containsOnlyDomainRelatedFields() throws JsonProcessingException {
        // given
        val cart = cartFactory.with("a cart", Collections.singletonList(Item.withName("an item")));
        val objectMapper = new ObjectMapper();
        val expectedSerializedObject = objectMapper.writeValueAsString(CartWithSerializableFieldsOnly.from(cart));

        // when
        val serializedObject = objectMapper.writeValueAsString(cart);

        // then
        assertThat(serializedObject).isEqualTo(expectedSerializedObject);
    }

    @Getter
    @AllArgsConstructor
    private static class CartWithSerializableFieldsOnly {
        private final UUID id;
        private final String name;
        private final List<Item> items;

        private static CartWithSerializableFieldsOnly from(final Cart cart) {
            return new CartWithSerializableFieldsOnly(cart.getId(), cart.getName(), cart.getItems());
        }
    }
}

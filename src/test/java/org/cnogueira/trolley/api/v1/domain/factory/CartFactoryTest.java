package org.cnogueira.trolley.api.v1.domain.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.cnogueira.trolley.api.v1.domain.Item;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeNotifier;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeNotifierFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CartFactoryTest {

    @Mock
    private StateChangeNotifierFactory stateChangeNotifierFactory;
    private ObjectMapper objectMapper;

    private CartFactory cartFactory;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        cartFactory = new CartFactory(stateChangeNotifierFactory, objectMapper);
        given(stateChangeNotifierFactory.createStateChangeNotifier()).willReturn(mock(StateChangeNotifier.class));
    }

    @Test
    public void from() {
        // given
        val cartCreateRequest = CartCreateRequest.withName("some name");

        // when
        val cart = cartFactory.from(cartCreateRequest);

        // then
        assertThat(cart.getId()).isNotNull();
        assertThat(cart.getName()).isEqualTo(cartCreateRequest.getName());
        assertThat(cart.getItems()).isEmpty();
        verifyNewStateChangeNotifierHasBeenCreated();
    }

    @Test
    public void withName() {
        // given
        val cartName = "another name";

        // when
        val cart = cartFactory.with(cartName);

        // then
        assertThat(cart.getId()).isNotNull();
        assertThat(cart.getName()).isEqualTo(cartName);
        assertThat(cart.getItems()).isEmpty();
        verifyNewStateChangeNotifierHasBeenCreated();
    }

    @Test
    public void withNameAndItems() {
        // given
        val cartName = "another name";
        val items = Arrays.asList(Item.withName("item a"), Item.withName("item b"));

        // when
        val cart = cartFactory.with(cartName, items);

        // then
        assertThat(cart.getId()).isNotNull();
        assertThat(cart.getName()).isEqualTo(cartName);
        assertThat(cart.getItems()).hasSize(2);
        items.forEach(item -> assertThat(cart.getItems()).contains(item));
        verifyNewStateChangeNotifierHasBeenCreated();
    }

    @Test
    public void withCartIdAndName() {
        // given
        val cartId = UUID.randomUUID();
        val cartName = "another name";

        // when
        val cart = cartFactory.with(cartId, cartName);

        // then
        assertThat(cart.getId()).isEqualTo(cartId);
        assertThat(cart.getName()).isEqualTo(cartName);
        assertThat(cart.getItems()).isEmpty();
        verifyNewStateChangeNotifierHasBeenCreated();
    }

    @Test
    public void withCartIdNameAndItems() {
        // given
        val cartId = UUID.randomUUID();
        val cartName = "another name";
        val items = Arrays.asList(Item.withName("item a"), Item.withName("item b"));

        // when
        val cart = cartFactory.with(cartId, cartName, items);

        // then
        assertThat(cart.getId()).isEqualTo(cartId);
        assertThat(cart.getName()).isEqualTo(cartName);
        assertThat(cart.getItems()).hasSize(2);
        items.forEach(item -> assertThat(cart.getItems()).contains(item));
        verifyNewStateChangeNotifierHasBeenCreated();
    }

    @Test
    public void cloneOf() {
        // given
        val cartId = UUID.randomUUID();
        val cartName = "another name";
        val items = Arrays.asList(Item.withName("item a"), Item.withName("item b"));
        val originalCart = cartFactory.with(cartId, cartName, items);

        // when
        val cart = cartFactory.cloneOf(originalCart);

        // then
        assertThat(cart).isEqualTo(originalCart);
        assertThat(cart).isNotSameAs(originalCart);
        verifyNewStateChangeNotifierHasBeenCreated();
    }

    @Test
    public void fromJson() throws IOException {
        // given
        val cartId = UUID.randomUUID();
        val cartName = "another name";
        val items = Arrays.asList(Item.withName("item a"), Item.withName("item b"));
        val originalCart = cartFactory.with(cartId, cartName, items);
        val originalCartAsJson = objectMapper.writeValueAsString(originalCart);

        // when
        val cart = cartFactory.fromJson(originalCartAsJson);

        // then
        assertThat(cart).isEqualTo(originalCart);
        assertThat(cart).isNotSameAs(originalCart);
        verifyNewStateChangeNotifierHasBeenCreated();
    }

    private void verifyNewStateChangeNotifierHasBeenCreated() {
        verify(stateChangeNotifierFactory, atLeastOnce()).createStateChangeNotifier();
    }
}

package org.cnogueira.trolley.api.v1.domain.factory.impl;

import lombok.val;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultCartFactoryTest {

    private DefaultCartFactory defaultCartFactory = new DefaultCartFactory();

    @Test
    public void from() {
        // given
        val cartCreateRequest = CartCreateRequest.withName("some name");

        // when
        val cart = defaultCartFactory.from(cartCreateRequest);

        // then
        assertThat(cart.getId()).isNotNull();
        assertThat(cart.getName()).isEqualTo(cartCreateRequest.getName());
        assertThat(cart.getItems()).isEmpty();
    }

    @Test
    public void withName() {
        // given
        val cartName = "another name";

        // when
        val cart = defaultCartFactory.withName(cartName);

        // then
        assertThat(cart.getId()).isNotNull();
        assertThat(cart.getName()).isEqualTo(cartName);
        assertThat(cart.getItems()).isEmpty();
    }
}

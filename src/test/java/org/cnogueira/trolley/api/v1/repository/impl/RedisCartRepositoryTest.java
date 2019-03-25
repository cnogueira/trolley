package org.cnogueira.trolley.api.v1.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObservable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import redis.clients.jedis.Jedis;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RedisCartRepositoryTest {

    private final CartFactory cartFactory = CartFactory.create();

    private Jedis redisClient;
    private ObjectMapper objectMapper;

    private RedisCartRepository redisCartRepository;

    @Before
    public void setUp() {
        redisClient = mock(Jedis.class);
        objectMapper = spy(ObjectMapper.class);

        redisCartRepository = new RedisCartRepository(redisClient, objectMapper);
    }

    @Test
    public void addCart() throws JsonProcessingException {
        // given
        val cart = cartFactory.with("a cart");
        val serializedCart = objectMapper.writeValueAsString(cart);

        // when
        redisCartRepository.addCart(cart);

        // then
        verify(redisClient, times(1)).set(eq(cart.getId().toString()), eq(serializedCart));
    }

    @Test
    public void getById() throws JsonProcessingException {
        // given
        val cart = cartFactory.with("a cart");
        val serializedCart = objectMapper.writeValueAsString(cart);
        given(redisClient.get(eq(cart.getId().toString()))).willReturn(serializedCart);

        // when
        val optionalCart = redisCartRepository.getById(cart.getId());

        // then
        assertThat(optionalCart).contains(cart);
    }

    @Test
    public void getById_returnsEmptyWhenProvidedAnUnknownId() {
        // given
        val unknownId = UUID.randomUUID();
        given(redisClient.get(eq(unknownId.toString()))).willReturn(null);

        // when
        val optionalCart = redisCartRepository.getById(unknownId);

        // then
        assertThat(optionalCart).isEmpty();
    }

    @Test
    public void replaceCart() throws JsonProcessingException {
        // given
        val cart = cartFactory.with("a cart");
        val serializedCart = objectMapper.writeValueAsString(cart);

        // when
        redisCartRepository.replaceCart(cart);

        // then
        verify(redisClient, times(1)).set(eq(cart.getId().toString()), eq(serializedCart));
    }

    @Test
    public void onStateChanged() throws JsonProcessingException {
        // given
        val cart = cartFactory.with("a cart");
        val serializedCart = objectMapper.writeValueAsString(cart);

        // when
        redisCartRepository.onStateChanged(cart);

        // then
        verify(redisClient, times(1)).set(eq(cart.getId().toString()), eq(serializedCart));
    }

    @Test(expected = IllegalArgumentException.class)
    public void onStateChanged_throwsIfCalledWithAnUnknownEmitter() {
        // given
        val unknownEmitter = mock(StateChangeObservable.class);

        // when
        redisCartRepository.onStateChanged(unknownEmitter);

        // then
        fail("CartRepository must throw when notified on state changed of something that's not a Cart");
    }
}

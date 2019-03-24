package org.cnogueira.trolley.api.v1.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.repository.CartRepository;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObservable;
import redis.clients.jedis.Jedis;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class RedisCartRepository implements CartRepository {

    private final Jedis redisClient;
    private final ObjectMapper objectMapper;

    @Override
    public void addCart(final Cart cart) {

    }

    @Override
    public Optional<Cart> getById(final UUID cartId) {
        return Optional.empty();
    }

    @Override
    public void replaceCart(final Cart cart) {

    }

    @Override
    public void onStateChanged(final StateChangeObservable emitter) {

    }
}

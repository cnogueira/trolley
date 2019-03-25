package org.cnogueira.trolley.api.v1.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.repository.CartRepository;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class RedisCartRepository implements CartRepository {

    private final Jedis redisClient;
    private final ObjectMapper objectMapper;

    @Override
    public void addCart(final Cart cart) {
        try {
            redisClient.set(cart.getId().toString(), objectMapper.writeValueAsString(cart));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to insert cart to redis");
        }
    }

    @Override
    public Optional<Cart> getById(final UUID cartId) {
        return Optional.ofNullable(redisClient.get(cartId.toString()))
            .map(serializedCart -> {
                try {
                    return objectMapper.readValue(serializedCart, Cart.class);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to deserialize cart object");
                }
            });
    }
}

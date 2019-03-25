package org.cnogueira.trolley.api.v1.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.cnogueira.trolley.api.v1.repository.CartRepository;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class RedisCartRepository implements CartRepository {

    private final Jedis redisClient;
    private final CartFactory cartFactory;
    private final ObjectMapper objectMapper;

    @Override
    public void addCart(final Cart cart) {
        log.info("About to add cart: " + cart + " to redis");
        try {
            redisClient.set(cart.getId().toString(), objectMapper.writeValueAsString(cart));
            log.info("Stored cart with id: " + cart.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to insert cart to redis");
        }
    }

    @Override
    public Optional<Cart> getById(final UUID cartId) {
        log.info("About to retrieve cartId: " + cartId + " from redis");
        return Optional.ofNullable(redisClient.get(cartId.toString()))
            .map(serializedCart -> {
                log.info("Got: {} from redis", serializedCart);
                try {
                    return cartFactory.fromJson(serializedCart);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to deserialize cart object");
                }
            });
    }
}

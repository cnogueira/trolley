package org.cnogueira.trolley;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.cnogueira.trolley.api.v1.domain.factory.CartFactory;
import org.cnogueira.trolley.api.v1.repository.CartRepository;
import org.cnogueira.trolley.api.v1.repository.impl.InMemoryCartRepository;
import org.cnogueira.trolley.api.v1.repository.impl.RedisCartRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.Jedis;

@Configuration
@SpringBootApplication
public class TrolleyApplication {

    private static final String PROPERTY_REDIS_URL = "REDIS_URL";
    private static final String PROPERTY_REDIS_PORT = "REDIS_URL";
    private static final Integer REDIS_DEFAULT_PORT = 6379;

    public static void main(String[] args) {
        SpringApplication.run(TrolleyApplication.class, args);
    }

    @Bean
    @ConditionalOnBean(CartFactory.class)
    CartRepository cartRepository(final Environment environment, final CartFactory cartFactory, final ObjectMapper objectMapper) {
        if (environment.containsProperty(PROPERTY_REDIS_URL)) {
            return redisCartRepository(environment, objectMapper);
        }

        return new InMemoryCartRepository(cartFactory);
    }

    private CartRepository redisCartRepository(final Environment environment, final ObjectMapper objectMapper) {
        val redisUrl = environment.getProperty(PROPERTY_REDIS_URL);

        val redisPort = environment.containsProperty(PROPERTY_REDIS_PORT)
            ? Integer.parseInt(environment.getProperty(PROPERTY_REDIS_PORT))
            : REDIS_DEFAULT_PORT;

        return new RedisCartRepository(new Jedis(redisUrl, redisPort), objectMapper);
    }
}

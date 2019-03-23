package org.cnogueira.trolley;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import lombok.val;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.dto.Cart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TrolleyApplicationTests {

    private static final String CARTS_API = "/api/v1/carts";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void allowsCartCreation() {
        // given
        val cartCreateRequest = CartCreateRequest.withName("Test Cart");

        // when
        val createCartResponse = restTemplate.postForEntity(CARTS_API, cartCreateRequest, Cart.class);

        // then
        assertThat(createCartResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createCartResponse.getBody()).isNotNull();
        assertThat(createCartResponse.getBody().getName()).isEqualTo(cartCreateRequest.getName());
    }

    @Test
    public void allowsCartRetrievalByCartId() {
        // given
        val cart = createCart("Test Cart");

        // when
        val cartDetailsResponse = restTemplate.getForEntity(CARTS_API + "/" + cart.getId(), Cart.class);

        // then
        assertThat(cartDetailsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cartDetailsResponse.getBody()).isNotNull();
        assertThat(cartDetailsResponse.getBody()).isEqualTo(cart);
    }

    private Cart createCart(final String cartName) {
        val createCartResponse = restTemplate
                .postForEntity(CARTS_API, CartCreateRequest.withName(cartName), Cart.class);

        return createCartResponse.getBody();
    }

}

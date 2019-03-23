package org.cnogueira.trolley.api.v1.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.cnogueira.trolley.api.v1.dto.Cart;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.exceptions.CartNotFoundException;
import org.cnogueira.trolley.api.v1.service.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(CartController.class)
public class CartControllerTest {

    private static final String CARTS_API = "/api/v1/carts";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @Test
    public void createCart_delegatesToCartService() throws Exception {
        // given
        val cartRequestBody = CartCreateRequest.withName("a name for the cart");
        val cart = Cart.from(cartRequestBody);
        given(cartService.createCart(eq(cartRequestBody))).willReturn(cart);

        // when
        val response = mockMvc.perform(post(CARTS_API)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(cartRequestBody)));

        // then
        response.andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("id").value(cart.getId().toString()))
                .andExpect(jsonPath("name").value(cartRequestBody.getName()));
    }

    @Test
    public void getCart_delegatesToCartService() throws Exception {
        // given
        val cart = Cart.builder()
                .id(UUID.randomUUID())
                .name("some name")
                .build();
        given(cartService.getCart(eq(cart.getId()))).willReturn(cart);

        // when
        val response = mockMvc.perform(get(CARTS_API + "/" + cart.getId()));

        // then
        response.andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("id").value(cart.getId().toString()))
                .andExpect(jsonPath("name").value(cart.getName()));
    }

    @Test
    public void getCart_respondsWithHttpNotFoundWhenProvidedIdIsNotFound() throws Exception {
        // given
        given(cartService.getCart(any())).willThrow(CartNotFoundException.class);

        // when
        val response = mockMvc.perform(get(CARTS_API + "/" + UUID.randomUUID()));

        // then
        response.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}

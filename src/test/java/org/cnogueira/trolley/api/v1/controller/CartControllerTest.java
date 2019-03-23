package org.cnogueira.trolley.api.v1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.Item;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.dto.ItemAddRequest;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        val cart = createRandomCartWith("some name");
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

    @Test
    public void addItem_respondsWithHttpNotFoundWhenProvidedCartIdDoesNotExists() throws Exception {
        // given
        given(cartService.addItem(any(), any())).willThrow(CartNotFoundException.class);
        val cartId = UUID.randomUUID();
        val itemAddRequest = ItemAddRequest.withName("item 1");

        // when
        val response = mockMvc.perform(addItemPostRequest(cartId, itemAddRequest));

        // then
        response.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void addItem_delegatesToCartService() throws Exception {
        // given
        val cart = createRandomCartWith("cart name", Arrays.asList("item 1", "item 2"));
        val itemAddRequest = ItemAddRequest.withName("item 2");
        val item = Item.from(itemAddRequest);
        given(cartService.addItem(eq(cart.getId()), eq(itemAddRequest))).willReturn(item);

        // when
        val response = mockMvc.perform(addItemPostRequest(cart.getId(), itemAddRequest));

        // then
        response.andExpect(status().is(HttpStatus.CREATED.value()))
            .andExpect(jsonPath("id").value(item.getId().toString()))
            .andExpect(jsonPath("name").value(itemAddRequest.getName()));
    }

    private MockHttpServletRequestBuilder addItemPostRequest(final UUID cartId,
                                                             final ItemAddRequest itemAddRequest) throws Exception {
        return post(CARTS_API + "/" + cartId + "/items")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(itemAddRequest));
    }

    private Cart createRandomCartWith(final String cartName) {
        return createRandomCartWith(cartName, Collections.emptyList());
    }

    private Cart createRandomCartWith(final String cartName, List<String> itemNames) {
        return Cart.builder()
            .id(UUID.randomUUID())
            .name(cartName)
            .items(itemNames.stream()
                .map(Item::withName)
                .collect(toList()))
            .build();
    }
}

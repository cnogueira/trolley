package org.cnogueira.trolley.api.v1.controller;

import lombok.AllArgsConstructor;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.Item;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.dto.ItemAddRequest;
import org.cnogueira.trolley.api.v1.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Cart createCart(@RequestBody final CartCreateRequest cartCreateRequest) {
        return cartService.createCart(cartCreateRequest);
    }

    @GetMapping(path = "/{cartId}")
    public Cart getCart(@PathVariable final UUID cartId) {
        return cartService.getCart(cartId);
    }

    @PostMapping(path = "/{cartId}/items")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Item addItem(@PathVariable final UUID cartId, @RequestBody final ItemAddRequest itemAddRequest) {
        return cartService.addItem(cartId, itemAddRequest);
    }
}

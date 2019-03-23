package org.cnogueira.trolley.api.v1.controller;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
}

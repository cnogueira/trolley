package org.cnogueira.trolley.api.v1.controller;

import lombok.AllArgsConstructor;
import lombok.val;
import org.cnogueira.trolley.api.v1.dto.Cart;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;
import org.cnogueira.trolley.api.v1.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody CartCreateRequest cartCreateRequest) {
        val cart = cartService.createCart(cartCreateRequest);

        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }
}

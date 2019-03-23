package org.cnogueira.trolley.api.v1.domain.factory;

import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;

public interface CartFactory {
    Cart from(CartCreateRequest cartCreateRequest);

    Cart withName(String cartName);
}

package org.cnogueira.trolley.api.v1;

import lombok.experimental.UtilityClass;
import org.cnogueira.trolley.api.v1.domain.Cart;
import org.cnogueira.trolley.api.v1.domain.Item;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@UtilityClass
public final class TestUtils {
    public static Cart createRandomCartWith(final String cartName) {
        return createRandomCartWith(cartName, Collections.emptyList());
    }

    public static Cart createRandomCartWith(final String cartName, final List<String> itemNames) {
        return Cart.builder()
            .id(UUID.randomUUID())
            .name(cartName)
            .items(itemNames.stream()
                .map(Item::withName)
                .collect(toList()))
            .build();
    }
}

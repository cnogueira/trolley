package org.cnogueira.trolley.api.v1.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public final class Cart {
    private final UUID id;
    private final String name;

    public static Cart from(final CartCreateRequest cartCreateRequest) {
        return Cart.builder()
                .id(UUID.randomUUID())
                .name(cartCreateRequest.getName())
                .build();
    }
}

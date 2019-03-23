package org.cnogueira.trolley.api.v1.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;

@Value
@Builder
@RequiredArgsConstructor
public final class Cart {
    private final UUID id;
    private final String name;

    @Builder.Default
    private final List<Item> items = new ArrayList<>();

    public static Cart from(final CartCreateRequest cartCreateRequest) {
        return Cart.builder()
                .id(UUID.randomUUID())
                .name(cartCreateRequest.getName())
                .build();
    }
}

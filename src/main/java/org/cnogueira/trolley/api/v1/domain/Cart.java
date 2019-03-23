package org.cnogueira.trolley.api.v1.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.cnogueira.trolley.api.v1.dto.CartCreateRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Cart {
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

    public void addItem(final Item item) {
    }
}

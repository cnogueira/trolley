package org.cnogueira.trolley.api.v1.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.cnogueira.trolley.api.v1.dto.ItemAddRequest;

import java.util.UUID;

@Value
@Builder
@RequiredArgsConstructor
public class Item {
    private final UUID id;
    private final String name;

    public static Item withName(final String itemName) {
        return Item.builder()
            .id(UUID.randomUUID())
            .name(itemName)
            .build();
    }

    public static Item from(final ItemAddRequest itemAddRequest) {
        return Item.builder()
            .id(UUID.randomUUID())
            .name(itemAddRequest.getName())
            .build();
    }
}

package org.cnogueira.trolley.api.v1.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.cnogueira.trolley.api.v1.repository.CartRepository;

import java.util.ArrayList;
import java.util.Collections;
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

    public void addItem(final Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addStateChangeObserver(final CartRepository cartRepository) {
    }
}

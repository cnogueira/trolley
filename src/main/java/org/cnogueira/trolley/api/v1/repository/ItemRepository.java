package org.cnogueira.trolley.api.v1.repository;

import org.cnogueira.trolley.api.v1.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ItemRepository {

    private final Map<UUID, Item> items = new HashMap<>();

    public void addItem(final Item item) {
        items.put(item.getId(), item);
    }

    public Optional<Item> getById(final UUID itemId) {
        return  Optional.ofNullable(items.get(itemId));
    }
}

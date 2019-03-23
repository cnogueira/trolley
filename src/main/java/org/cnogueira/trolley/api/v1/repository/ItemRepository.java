package org.cnogueira.trolley.api.v1.repository;

import org.cnogueira.trolley.api.v1.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ItemRepository {
    public void addItem(final Item item) {

    }

    public Optional<Item> getById(final UUID itemId) {
        return  null;
    }
}

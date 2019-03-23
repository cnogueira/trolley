package org.cnogueira.trolley.api.v1.repository;

import lombok.val;
import org.cnogueira.trolley.api.v1.domain.Item;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRepositoryTest {

    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        itemRepository = new ItemRepository();
    }

    @Test
    public void emptyByDefault() {
        // when
        val optionalItem = itemRepository.getById(UUID.randomUUID());

        //then
        assertThat(optionalItem).isEmpty();
    }

    @Test
    public void allowsGetByIdAfterAddingIt() {
        // given
        val item = Item.withName("just an item");

        // when
        itemRepository.addItem(item);
        val optionalCart = itemRepository.getById(item.getId());

        //then
        assertThat(optionalCart).contains(item);
    }
}

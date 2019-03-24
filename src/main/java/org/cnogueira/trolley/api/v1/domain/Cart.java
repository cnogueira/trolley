package org.cnogueira.trolley.api.v1.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.cnogueira.trolley.api.v1.repository.CartRepository;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeNotifier;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObservable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Cart implements StateChangeObservable {

    @EqualsAndHashCode.Exclude
    private final StateChangeNotifier stateChangeNotifier;

    @Getter
    private final UUID id;

    @Getter
    private final String name;

    private final List<Item> items = new ArrayList<>();

    public void addItem(final Item item) {
        items.add(item);

        stateChangeNotifier.notifyStateChanged(this);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addStateChangeObserver(final CartRepository cartRepository) {
    }
}

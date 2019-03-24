package org.cnogueira.trolley.api.v1.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeNotifier;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObservable;
import org.cnogueira.trolley.api.v1.service.stateChange.StateChangeObserver;

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

    @Override
    public void subscribeStateChangeObserver(final StateChangeObserver observer) {
        stateChangeNotifier.subscribe(observer);
    }

    @Override
    public void unsubscribeStateChangeObserver(final StateChangeObserver observer) {
        stateChangeNotifier.unsubscribe(observer);
    }
}

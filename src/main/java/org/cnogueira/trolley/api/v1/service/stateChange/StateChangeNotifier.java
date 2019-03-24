package org.cnogueira.trolley.api.v1.service.stateChange;

import java.util.HashSet;
import java.util.Set;

public class StateChangeNotifier {

    private Set<StateChangeObserver> subscribers = new HashSet<>();

    public void notifyStateChanged(final StateChangeObservable emitter) {
        subscribers.forEach(subscriber -> subscriber.onStateChanged(emitter));
    }

    public void subscribe(final StateChangeObserver observer) {
        subscribers.add(observer);
    }
}

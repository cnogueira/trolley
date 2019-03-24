package org.cnogueira.trolley.api.v1.service.stateChange;

public interface StateChangeObservable {
    void subscribeStateChangeObserver(StateChangeObserver observer);
    void unsubscribeStateChangeObserver(StateChangeObserver observer);
}

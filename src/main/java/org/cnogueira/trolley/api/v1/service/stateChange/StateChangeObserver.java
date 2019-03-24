package org.cnogueira.trolley.api.v1.service.stateChange;

public interface StateChangeObserver {
    void onStateChanged(StateChangeObservable emitter);
}

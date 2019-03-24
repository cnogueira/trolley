package org.cnogueira.trolley.api.v1.service.stateChange;

import lombok.val;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StateChangeNotifierTest {

    private final StateChangeNotifier stateChangeNotifier = new StateChangeNotifier();

    @Test
    public void notifyStateChanged() {
        // given
        val observer = mock(StateChangeObserver.class);
        val emitter = mock(StateChangeObservable.class);

        // when
        stateChangeNotifier.subscribe(observer);
        stateChangeNotifier.notifyStateChanged(emitter);

        // then
        verify(observer, times(1)).onStateChanged(same(emitter));
    }

    @Test
    public void notifyAllObserversOnStateChange() {
        // given
        val observer1 = mock(StateChangeObserver.class);
        val observer2 = mock(StateChangeObserver.class);
        val emitter = mock(StateChangeObservable.class);

        // when
        stateChangeNotifier.subscribe(observer1);
        stateChangeNotifier.subscribe(observer2);
        stateChangeNotifier.notifyStateChanged(emitter);

        // then
        verify(observer1, times(1)).onStateChanged(same(emitter));
        verify(observer2, times(1)).onStateChanged(same(emitter));
    }

    @Test
    public void doesNotNotifyOnStateChangeUnsubscribedObservers() {
        // given
        val observer1 = mock(StateChangeObserver.class);
        val observer2 = mock(StateChangeObserver.class);
        val emitter = mock(StateChangeObservable.class);

        // when
        stateChangeNotifier.subscribe(observer1);
        stateChangeNotifier.subscribe(observer2);
        stateChangeNotifier.unsubscribe(observer2);
        stateChangeNotifier.notifyStateChanged(emitter);

        // then
        verify(observer1, times(1)).onStateChanged(same(emitter));
        verify(observer2, times(0)).onStateChanged(same(emitter));
    }
}

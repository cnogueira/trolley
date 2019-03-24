package org.cnogueira.trolley.api.v1.service.stateChange;

import org.springframework.stereotype.Service;

@Service
public class StateChangeNotifierFactory {
    public StateChangeNotifier createStateChangeNotifier() {
        return new StateChangeNotifier();
    }
}

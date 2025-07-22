package com.mvsim.model.observer;

import com.mvsim.model.ventilator.MostRecentTickData;

public interface SimMgrObserver {
     /**
     * This method should be called within Observable.notifyObservers()
     * @param data
     */
    void update(MostRecentTickData data);
}

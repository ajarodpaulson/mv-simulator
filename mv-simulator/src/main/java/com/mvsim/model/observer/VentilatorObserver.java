package com.mvsim.model.observer;

import com.mvsim.model.ventilator.Ventilator;

/**
 * Represents an observer in the Observer Design Pattern
 */
public interface VentilatorObserver {
    /**
     * This method should be called within Observable.notifyObservers()
     * @param vtr The object being observed
     */
    void update(Ventilator vtr);
}

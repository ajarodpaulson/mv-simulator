package com.mvsim.model.observer;

/**
 * Represents an observer in the Observer Design Pattern
 */
public interface Observer<T> {
    /**
     * This method should be called within Observable.notifyObservers()
     * @param observable The object being observed
     */
    void update(T observable);
}

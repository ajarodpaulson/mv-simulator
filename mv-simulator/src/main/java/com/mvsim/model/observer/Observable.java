package com.mvsim.model.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an observable in the Observer Design Pattern
 */
public abstract class Observable {
    protected List<VentilatorObserver> observers;

    public List<VentilatorObserver> getObservers() {
        return observers;
    }

    /**
     * Constructor creates an empty list of observers
     */
    public Observable() {
        observers = new ArrayList<>();
    }

    /**
     * Adds an observer to the list of observers
     * 
     * @param o the observer to be added
     */
    public void addObserver(VentilatorObserver o) {
        observers.add(o);
    }

    /**
     * Notifies the observers when a change occurs
     * in the status of this Observable.
     */
    public abstract void notifyObservers();
}

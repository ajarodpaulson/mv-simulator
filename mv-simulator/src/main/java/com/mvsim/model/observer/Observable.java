package com.mvsim.model.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an observable in the Observer Design Pattern
 * @param <T> The type of observable
 */
public abstract class Observable<T> {
    protected List<Observer<T>> observers;

    public List<Observer<T>> getObservers() {
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
    public void addObserver(Observer<T> o) {
        observers.add(o);
    }

    /**
     * Notifies the observers when a change occurs
     * in the status of this Observable.
     */
    public abstract void notifyObservers();
}

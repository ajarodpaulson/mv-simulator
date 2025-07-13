package com.mvsim.model.ventilator.settings;

import com.mvsim.model.ventilator.Ventilator;

/**
 * Represents a strategy for Triggering behaviour in which no triggering is
 * allowed.
 */
public class NoTrigger implements Trigger {

    @Override
    public boolean hasTriggered(Ventilator ventilator) {
        // This trigger strategy never fires.
        return false;
    }
}

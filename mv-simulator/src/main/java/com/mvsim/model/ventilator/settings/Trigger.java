package com.mvsim.model.ventilator.settings;

import com.mvsim.model.ventilator.Ventilator;

/**
 * Represents trigger behaviour in the Strategy pattern.
 */
public interface Trigger {
    public static final String NAME = "Trigger";

    public boolean hasTriggered(Ventilator vtr);
}

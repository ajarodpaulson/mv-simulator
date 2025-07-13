package com.mvsim.model.ventilator.mode;

/**
 * Represents targeting scheme behaviour for a ventilation mode in the Strategy
 * pattern. An implementing algorithm is responsible for updating the target for 
 * the current control-loop tick.
 */
public interface TargetingScheme<V extends ModeControlVariable> {
    public void updateTarget();
}

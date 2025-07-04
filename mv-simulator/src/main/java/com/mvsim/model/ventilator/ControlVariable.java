package com.mvsim.model.ventilator;

/**
 * Represents the control variable behaviour in the Strategy pattern. Implementing
 * algorithms are responsible for using target and sequencing information to drive 
 * the ventilator's hardware for the current control-loop tick.
 */
public interface ControlVariable <V extends ModeControlVariable> {
    public void actuate(Ventilator vtr);
}

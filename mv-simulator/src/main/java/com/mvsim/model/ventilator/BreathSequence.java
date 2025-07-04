package com.mvsim.model.ventilator;

/**
 * Represents the breath sequencing behaviour for a ventilation mode in the Strategy
 * pattern. Implementing algorithms are responsible for determining the current phase 
 * of the breath cycle 
 */
public interface BreathSequence {
    public Trigger getTrigger();

    public void determinePhase(Ventilator vtr);
}

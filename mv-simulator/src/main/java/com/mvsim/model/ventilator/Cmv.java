package com.mvsim.model.ventilator;

/**
 * Represents an algorithm for breath sequence behaviour in the strategy
 * pattern.
 * This algorithm delivers only mandatory breaths.
 */
public class Cmv implements BreathSequence {

    @Override
    public Trigger getTrigger() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTriggerType'");
    }

    /**
     * Determines the phase of the breath using the following algorithm:
     * 1. Uses the trigger and corresponding sensor, if applicable, to determine
     * whether a new cycle should be initiated.
     * 2. Uses the phase on the previous tick, relevant contextual information, and
     * active cycling criteria to determine the phase.
     */
    @Override
    public void determinePhase(Ventilator vtr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'determinePhase'");
    }

}

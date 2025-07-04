package com.mvsim.model.ventilator;

/**
 * Represents an algorithm for initiating the breath cycle per a set respiratory
 * rate
 */
public class InitCycleTimed implements InitCycleBehaviour {

    /**
     * Initiates a breath cycle according to the ventiator's set respiratory 
     * rate by calling the active ventilation mode's inhale method
     * TODO: where do I update the state of the LungSim
     */
    @Override
    public void initCycle(Ventilator ventilator) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initCycle'");
    }
}

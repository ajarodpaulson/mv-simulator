package com.mvsim.model.ventilator;

/**
 * TODO: improve the design of this class as more ventilation modes are added...
 * use Chatburn's taxonomy?
 * Represents a ventilation mode
 */
public abstract class VentilationMode<V extends ModeControlVariable> {
    Ventilator vtr;

    BreathSequence seq;
    TargetingScheme<V> ts;
    ControlVariable<V> cv;

    // InitCycleBehaviour initCycleBehaviour;
    // InhaleBehaviour inhaleBehaviour;
    // ExhaleBehaviour exhaleBehaviour;
    // EndCycleBehaviour endCycleBehaviour;
    // XXX: protected abstract void setup(); // necessary?

    // TODO: enabledAlarms() // a list of alarms that should be enabled for this
    // mode

    VentilationMode(Ventilator vtr, BreathSequence breathSequence, TargetingScheme<V> targetingScheme,
            ControlVariable<V> controlVariable) {
        this.vtr = vtr;
        this.seq = breathSequence;
        this.ts = targetingScheme;
        this.cv = controlVariable;
    }

    public void tick() {
        seq.determinePhase(vtr);
        ts.updateTarget(vtr);
        cv.actuate(vtr);
    }
}
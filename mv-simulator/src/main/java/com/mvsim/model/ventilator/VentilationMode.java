package com.mvsim.model.ventilator;

import com.mvsim.model.ventilator.settings.Settings;

/**
 * Represents a ventilation mode that is composed by combining breath sequence,
 * targeting scheme, and control variable behaviour.
 * The ventilation mode is also responsible for orchestrating these three
 * behavioural components during each tick once ventilation has started.
 */
public abstract class VentilationMode<V extends ModeControlVariable> {
    protected Ventilator vtr;

    protected BreathSequence seq;
    protected TargetingScheme<V> ts;
    protected ControlVariable<V> cv;

    protected Settings settings;

    VentilationMode(Ventilator vtr, ControlVariable<V> controlVariable, BreathSequence breathSequence,
            TargetingScheme<V> targetingScheme, Settings settings) {
        this.vtr = vtr;
        this.seq = breathSequence;
        this.ts = targetingScheme;
        this.cv = controlVariable;
        this.settings = settings;
    }

    public void tick() {
        seq.determinePhase(vtr);
        ts.updateTarget(vtr);
        cv.actuate(vtr);
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}

// InitCycleBehaviour initCycleBehaviour;
// InhaleBehaviour inhaleBehaviour;
// ExhaleBehaviour exhaleBehaviour;
// EndCycleBehaviour endCycleBehaviour;
// XXX: protected abstract void setup(); // necessary?

// TODO: enabledAlarms() // a list of alarms that should be enabled for this
// mode
package com.mvsim.model.ventilator.mode;

import com.mvsim.model.ventilator.Ventilator;
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

    /*
     * XXX: feel like these don't belong in this class.
     */
    private int tick;
    private boolean isInInspiratoryPhase;
    public static final int tickPeriodInMS = 500;

    public boolean getIsInInspiratoryPhase() {
        return isInInspiratoryPhase;
    }

    public int getTimeInPhaseInMS() {
        return tick * tickPeriodInMS;
    }

    public void setIsInInspiratoryPhase(boolean isInInspiratoryPhase) {
        if (this.getIsInInspiratoryPhase() == isInInspiratoryPhase) {
            return;
        }
        this.isInInspiratoryPhase = isInInspiratoryPhase;
        resetTick();
    }

    VentilationMode(Ventilator vtr, ControlVariable<V> controlVariable, BreathSequence breathSequence,
            TargetingScheme<V> targetingScheme, Settings settings) {
        this.vtr = vtr;
        this.seq = breathSequence;
        this.ts = targetingScheme;
        this.cv = controlVariable;
        this.settings = settings;
        tick = 0;
    }

    private void resetTick() {
        tick = 0;
    }

    public void tick() {
        seq.determinePhase();
        ts.updateTarget();
        cv.actuate();

        vtr.notifyObservers();
        
        tick++;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public float getTickPeriod() {
        return tickPeriodInMS;
    }
}

// InitCycleBehaviour initCycleBehaviour;
// InhaleBehaviour inhaleBehaviour;
// ExhaleBehaviour exhaleBehaviour;
// EndCycleBehaviour endCycleBehaviour;
// XXX: protected abstract void setup(); // necessary?

// TODO: enabledAlarms() // a list of alarms that should be enabled for this
// mode
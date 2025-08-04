package com.mvsim.model.ventilator.mode;

import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.settings.Setting;
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
    private int ticksInCurrentBreathPhase;
    private int ticksInCurrentBreathCycle = 0;
    private int ticksInPreviousBreathCycle;
    private boolean isInInspiratoryPhase = true;
    private int ticksInPreviousExpPhase;

    public static final int TICK_PERIOD_IN_MS = 50;

    public int getTicksInPreviousBreathCycle() {
        return ticksInPreviousBreathCycle;
    }

    public int getTicksInPreviousExpPhase() {
        return ticksInPreviousExpPhase;
    }

    public int getTicksInCurrentBreathPhase() {
        return ticksInCurrentBreathPhase;
    }

    public boolean getIsInInspiratoryPhase() {
        return isInInspiratoryPhase;
    }

    public int getTimeInPhaseInMS() {
        return ticksInCurrentBreathPhase * TICK_PERIOD_IN_MS;
    }

    public void setIsInInspiratoryPhase(boolean isNextPhaseInspiratoryPhase) {
        if (this.isInInspiratoryPhase == isNextPhaseInspiratoryPhase) {
            return;
        }

        if (!this.isInInspiratoryPhase && isNextPhaseInspiratoryPhase) {
            ticksInPreviousExpPhase = ticksInCurrentBreathPhase;
            ticksInPreviousBreathCycle = ticksInCurrentBreathCycle;
            ticksInCurrentBreathCycle = 0;
        }

        this.isInInspiratoryPhase = isNextPhaseInspiratoryPhase;
        resetTick();
    }

    VentilationMode(Ventilator vtr, ControlVariable<V> controlVariable, BreathSequence breathSequence,
            TargetingScheme<V> targetingScheme, Settings settings) {
        this.vtr = vtr;
        this.seq = breathSequence;
        this.ts = targetingScheme;
        this.cv = controlVariable;
        this.settings = settings;
        ticksInCurrentBreathPhase = 0;
    }

    private void resetTick() {
        ticksInCurrentBreathPhase = 0;
    }

    public void tick() {
        seq.determinePhase();
        ts.updateTarget();
        cv.actuate();

        vtr.notifyObservers();

        ticksInCurrentBreathPhase++;
        ticksInCurrentBreathCycle++;
    }

    public boolean getDidPhaseTransitionFromInspToExp() {
        return seq.getDidPhaseTransitionFromInspToExp();
    }

    public boolean getDidPhaseTransitionFromExpToInsp() {
        return seq.getDidPhaseTransitionFromExpToInsp();
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public float getTickPeriod() {
        return TICK_PERIOD_IN_MS;
    }

    public Setting getSetting(String name) {
        return settings.getSetting(name);
    }

    // protected abstract boolean hasFullyConfiguredSettings(); // not needed for
    // now because all setting strategies will have default values
}

// InitCycleBehaviour initCycleBehaviour;
// InhaleBehaviour inhaleBehaviour;
// ExhaleBehaviour exhaleBehaviour;
// EndCycleBehaviour endCycleBehaviour;
// XXX: protected abstract void setup(); // necessary?

// TODO: enabledAlarms() // a list of alarms that should be enabled for this
// mode
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
    private int tickCounter;
    public int getTickCounter() {
        return tickCounter;
    }

    private boolean isInInspiratoryPhase = true;
    public static final int TICK_PERIOD_IN_MS = 50;

    public boolean getIsInInspiratoryPhase() {
        return isInInspiratoryPhase;
    }

    public int getTimeInPhaseInMS() {
        return tickCounter * TICK_PERIOD_IN_MS;
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
        tickCounter = 0;
    }

    private void resetTick() {
        tickCounter = 0;
    }


    public void tick() {
        seq.determinePhase();
        ts.updateTarget();
        cv.actuate();

        vtr.notifyObservers();
        
        tickCounter++;
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

    // protected abstract boolean hasFullyConfiguredSettings(); // not needed for now because all setting strategies will have default values
}

// InitCycleBehaviour initCycleBehaviour;
// InhaleBehaviour inhaleBehaviour;
// ExhaleBehaviour exhaleBehaviour;
// EndCycleBehaviour endCycleBehaviour;
// XXX: protected abstract void setup(); // necessary?

// TODO: enabledAlarms() // a list of alarms that should be enabled for this
// mode
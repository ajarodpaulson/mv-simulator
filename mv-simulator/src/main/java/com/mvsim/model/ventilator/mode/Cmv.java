package com.mvsim.model.ventilator.mode;

import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.settings.InspiratoryTime;
import com.mvsim.model.ventilator.settings.RespiratoryRate;
import com.mvsim.model.ventilator.settings.Settings;
import com.mvsim.model.ventilator.settings.Trigger;

/**
 * Represents an algorithm for breath sequence behaviour in the strategy
 * pattern. This algorithm delivers only mandatory breaths.
 */
public class Cmv implements BreathSequence {
    Ventilator vtr;

    Cmv(Ventilator vtr) {
        this.vtr = vtr;
    }

    /**
     * Determines the phase of the breath using the following algorithm:
     * 1. Uses the trigger and corresponding sensor, if applicable, to determine
     * whether a new cycle should be initiated.
     * 2. Uses the phase on the previous tick, relevant contextual information, and
     * active cycling criteria to determine the phase.
     */
    @Override
    public void determinePhase() {
        VentilationMode<?> activeMode = vtr.getController().getActiveMode();
        Settings settings = activeMode.getSettings();
        Trigger trigger = settings.getTrigger();
        float iTime = (float) settings.getSetting(InspiratoryTime.NAME).getValue();
        float breathCycleDuration = 60f / settings.getSetting(RespiratoryRate.NAME).getValue().intValue();
        float eTime = breathCycleDuration - iTime;

        if (trigger.hasTriggered(vtr)) {
            activeMode.setIsInInspiratoryPhase(true);
        } else if (activeMode.getIsInInspiratoryPhase()) {
            if (activeMode.getTimeInPhaseInMS() / 1000f >= iTime) {
                activeMode.setIsInInspiratoryPhase(false);
            }
        } else {
            if (activeMode.getTimeInPhaseInMS() / 1000f >= eTime) {
                activeMode.setIsInInspiratoryPhase(true);
            }
        }
    }
}

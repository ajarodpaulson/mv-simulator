package com.mvsim.model.ventilator;

import java.util.Set;

import com.mvsim.model.exception.ActiveModeNotSetException;
import com.mvsim.model.exception.PreconditionViolatedException;
import com.mvsim.model.ventilator.mode.ModeTAG;
import com.mvsim.model.ventilator.mode.VentilationMode;
import com.mvsim.model.ventilator.settings.Settings;

/**
 * The ventilator's controller is responsible for serving as the intermediary
 * between clients of the ventilator and the ventilator itself. The controller
 * accepts user input and uses it to configure the ventilator. The controller is
 * also responsible for serving pertinent information from the ventilator to the
 * user/simulation manager.
 */
public class VentilatorController {
    Ventilator vtr;
    private float systemVolumeChange = 0f;

    public VentilatorController(Ventilator vtr) {
        this.vtr = vtr;
    }

    /**
     * Sets the active ventilation mode using the supplied mode TAG its
     * corresponding default settings.
     * 
     * @param selectedModeTAG
     */
    public void setActiveModeWithCorrespondingDefaultSettings(ModeTAG selectedModeTAG) {
        vtr.setActiveMode(vtr.getModeTable().getMode(selectedModeTAG));
    }

    /**
     * Applies the supplied settings to the selected mode.
     * 
     * @throws PreconditionViolatedException if mode not yet selected
     */
    public void setModeSettings(Settings settings) throws PreconditionViolatedException {
        if (vtr.getActiveMode() == null) {
            throw new PreconditionViolatedException("Mode needs to be selected first.");
        }

        vtr.getActiveMode().setSettings(settings);
    }

    public VentilationMode<?> getActiveMode() {
        return vtr.getActiveMode();
    }

    /**
     * Communicates to the ventilator that the user has pressed the start
     * ventilation button.
     * 
     * @throws ActiveModeNotSetException
     */
    public void enableVentilation() throws ActiveModeNotSetException {
        vtr.enableVentilation();
    }

    /**
     * Stops the ventilator.
     * 
     * @throws PreconditionViolatedException if the ventilator is not currently
     *                                       ventilating
     */
    public void stopVentilation() {
        vtr.disableVentilation();
    }

    /**
     * Advances the simulation by one step by ticking the active mode.
     */
    public void tick() {
        vtr.tick();
    }

    public Set<ModeTAG> getAvailableModes() {
        return vtr.getAvailableModes();
    }

    public boolean getIsVentilating() {
        return vtr.getIsVentilationEnabled();
    }

    public float getCurrentSystemPressure() {
        return vtr.getPressureSensor().getCurrentSystemPressure();
    }

    public float getCurrentSystemFlowrate() {
        if (vtr.getActiveMode().getIsInInspiratoryPhase()) {
            return vtr.getInspFlowSensor().getLatestInspiratoryFlowReading();
        } else {
            return vtr.getExpFlowSensor().getCurrentExpiratoryFlow();
        }
    }

    public float getCurrentSystemVolumeChange() {
        if (vtr.getActiveMode().getIsInInspiratoryPhase()) {
            systemVolumeChange += vtr.getInspFlowSensor().getLatestInspiratoryFlowReading() * (VentilationMode.TICK_PERIOD_IN_MS / 1000f);
            return systemVolumeChange;
        } else {
            systemVolumeChange -= vtr.getExpFlowSensor().getCurrentExpiratoryFlow() * (VentilationMode.TICK_PERIOD_IN_MS / 1000f);
            return systemVolumeChange;
        }
    }

    public int getTickPeriodInMs() {
        return VentilationMode.TICK_PERIOD_IN_MS;
    }
}

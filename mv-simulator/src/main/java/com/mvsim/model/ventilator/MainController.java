package com.mvsim.model.ventilator;

import java.util.HashMap;
import java.util.Map;

import com.mvsim.model.exception.PreconditionViolatedException;
import com.mvsim.model.ventilator.settings.Settings;

/**
 * The ventilator's main controller is responsible for accepting user input and
 * using it to configure the ventilator.
 */
public class MainController {
    Ventilator vtr;

    private VentilationMode<?> selectedMode;

    public void setMode(ModeType selectedModeType) {
        this.selectedMode = vtr.getModeTable().getMode(selectedModeType);
    }

    /**
     * Applies the supplied settings to the selected mode.
     * 
     * @throws PreconditionViolatedException if mode not yet selected
     */
    public void setModeSettings(Settings settings) throws PreconditionViolatedException {
        if (selectedMode == null) {
            throw new PreconditionViolatedException("Mode needs to be selected first.");
        }

        selectedMode.setSettings(settings);
    }

    /**
     * Sets the active ventilation mode using the specified control variable,
     * breath sequence, and targeting scheme.
     */
    public void setActiveMode() {
        vtr.setActiveMode(selectedMode);
    }
}

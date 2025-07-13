package com.mvsim.model.ventilator.mode;

import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.settings.Peep;

/**
 * Represents an algorithm for the control variable behaviour in the Strategy
 * pattern.
 * This algorithm uses target and sequencing information to control volume.
 */
public class VolumeControl implements ControlVariable<VolumeBased> {

    private Ventilator vtr;

    public VolumeControl(Ventilator vtr) {
        this.vtr = vtr;
    }

    /**
     * Uses the current target information and sequencing information to actuate the
     * ventilator.
     */
    @Override
    public void actuate() {
        VentilationMode<?> activeMode =  vtr.getActiveMode();
        if (!activeMode.getIsInInspiratoryPhase()) {
            vtr.getActuator().regulatePressure((float) activeMode.getSettings().getSetting(Peep.NAME).getValue());
        } else {
            vtr.getActuator().deliverVolume(((VolumeTargeter) activeMode).getVolumeTarget());
        }
    }
}

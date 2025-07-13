package com.mvsim.model.ventilator.mode;

import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.settings.InspiratoryTime;
import com.mvsim.model.ventilator.settings.Settings;
import com.mvsim.model.ventilator.settings.TidalVolume;

public class VolumeSetPoint implements TargetingScheme<VolumeBased> {

    private Ventilator vtr;
    

    public VolumeSetPoint(Ventilator vtr) {
        this.vtr = vtr;
    }

    /**
     * Updates the target with the amount of tidal volume to be delivered during the
     * next tick. For now, each tick delivers a constant amount of tidal volume.
     * TODO: tests
     */
    /*
     * TODO: Updates the target for the next tick using the following algorithm:
     * 1. If the breath cycle is in it's expiratory phase, the target should be that
     * which is required for the first tick of the inspiratory phase.
     * 2. Otherwise, uses contextual information to update the pressure/flow target
     * to be delivered during this tick
     */

    /*
     * To set its targets, it needs to know what its targets are.
     */
    @Override
    public void updateTarget() {
        // XXX
        @SuppressWarnings("unchecked")
        VentilationMode<VolumeBased> activeMode = (VentilationMode<VolumeBased>) vtr.getActiveMode();
        Settings settings = activeMode.getSettings();
        float conversionFactor = activeMode.getTickPeriod()
                / (float) settings.getSetting(InspiratoryTime.NAME).getValue();
        float tidalVolumePerTick = (float) settings.getSetting(TidalVolume.NAME).getValue() * conversionFactor;
        ((VolumeTargeter) activeMode).setVolumeTarget(tidalVolumePerTick);
    }

}

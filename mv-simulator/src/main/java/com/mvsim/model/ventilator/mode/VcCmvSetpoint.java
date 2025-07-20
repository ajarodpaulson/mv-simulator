package com.mvsim.model.ventilator.mode;

import com.mvsim.model.ventilator.Ventilator;

public class VcCmvSetpoint extends VentilationMode<VolumeBased> implements VolumeTargeter {
    
    private float volumeTarget = 0;
    private float volumeDeliveredInCurrentInspiratoryPhase = 0;

    public float getVolumeDeliveredInCurrentInspiratoryPhase() {
        return volumeDeliveredInCurrentInspiratoryPhase;
    }

    public void setVolumeDeliveredInCurrentInspiratoryPhase() {
        this.volumeDeliveredInCurrentInspiratoryPhase += vtr.getLatestFlowSensorReading() * (VentilationMode.TICK_PERIOD_IN_MS * 1000);
    }

    @Override
    public float getVolumeTarget() {
        return volumeTarget;
    }

    @Override
    public void setVolumeTarget(float volumeTarget) {
        this.volumeTarget = volumeTarget;
    }


    public VcCmvSetpoint(Ventilator vtr) {
        super(vtr, new VolumeControl(vtr), new Cmv(vtr), new VolumeSetPoint(vtr), new VcCmvSetpointSettings());
    }
}
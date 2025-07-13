package com.mvsim.model.ventilator.mode;

public interface VolumeTargeter {
    public float getVolumeTarget();
    public void setVolumeTarget(float volumeTarget);
    public float getVolumeDeliveredInCurrentInspiratoryPhase();
}

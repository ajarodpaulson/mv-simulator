package com.mvsim.model.ventilator;

/**
 * Represents an algorithm for the control variable behaviour in the Strategy pattern.
 * This algorithm uses target and sequencing information to control volume.
 */
public class VolumeControl implements ControlVariable<VolumeBased> {

    /**
     * Uses the current target information and sequencing information to actuate the ventilator.
     */
    @Override
    public void actuate(Ventilator vtr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
}

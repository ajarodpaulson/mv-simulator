package com.mvsim.model.ventilator;

public class VolumeSetPoint implements TargetingScheme<VolumeBased> {

    /**
     * Updates the target for the next tick using the following algorithm:
     * 1. If the breath cycle is in it's expiratory phase, the target should be that which is required for the first tick of the inspiratory phase.
     * 2. Otherwise, uses contextual information to update the pressure/flow target to be delivered during this tick
     */

     /*
      * To set its targets, it needs to know what its targets are.
      */
    @Override
    public void updateTarget(Ventilator vtr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTarget'");
    }
    
}

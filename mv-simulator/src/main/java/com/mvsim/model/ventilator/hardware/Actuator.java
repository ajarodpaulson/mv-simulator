package com.mvsim.model.ventilator.hardware;

import com.mvsim.model.ventilator.Ventilator;

/**
 * Represents an idealized ventilator component capable of regulating circuit
 * pressure or outputting a specified volume.
 * 
 * In this application, the actuator is also responsible for informing the lungs
 * of the pressure/volume change.
 */
public class Actuator {
    Ventilator vtr;
    float lastVolumeBolusDelivered; // XXX

    public Actuator(Ventilator vtr) {
        this.vtr = vtr;
    }

    /**
     * Regulates the vtr's pressure to that specified. Always updates the
     * ventilator's lung simulator when called.
     */
    public void regulatePressure(float vtrPressure) {
        vtr.getLungSim().equilibratePressure(vtrPressure);
    }

    /**
     * Delivers the specified volume to the circuit. If applicable, updates the lung
     * simulator conntected to the ventilator appropriately.
     */
    public void deliverVolume(float volumeToDeliver) {
        vtr.getLungSim().addVolume(volumeToDeliver);
        lastVolumeBolusDelivered = volumeToDeliver;
    }

	public float getLastVolumeBolusDelivered() {
		return lastVolumeBolusDelivered;
	}

    public void instantlyPressurize(float peep) {
        vtr.getLungSim().instantlyPressurize(peep);
    }
}

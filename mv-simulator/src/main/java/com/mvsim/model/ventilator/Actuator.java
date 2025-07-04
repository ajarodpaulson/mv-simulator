package com.mvsim.model.ventilator;

/**
 * Represents an idealized ventilator component capable of regulating circuit
 * pressure or outputting a specified volume.
 * 
 * In this application, the actuator is also responsible for informing the lungs
 * of the pressure/volume change.
 */
public class Actuator {
    Ventilator vtr;

    Actuator(Ventilator vtr) {
        this.vtr = vtr;
    }

    /**
     * Changes the circuit pressure to that specified. If applicable, updates the
     * lung simulator conntected to the ventilator appropriately.
     */
    public void setPressure() {

    }

    /**
     * Delivers the specified volume to the circuit. If applicable, updates the lung
     * simulator conntected to the ventilator appropriately.
     */
    public void deliverVolume() {

    }
}

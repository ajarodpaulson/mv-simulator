package com.mvsim.model.ventilator.hardware;

import com.mvsim.model.observer.VentilatorObserver;
import com.mvsim.model.ventilator.Ventilator;

/**
 * Represents a pressure sensor in a ventilator. Pressure-sensing is faked here
 * by having the sensor ask the lung profile directly how much pressure it is
 * feeling. Note
 * as well that this assumes an idealized pt-vtr system that is closed and
 * depends only on the patient.
 */
public class PressureSensor implements VentilatorObserver {
    private float currentSystemPressure = 0;

    public float getCurrentSystemPressure() {
        return currentSystemPressure;
    }

    @Override
    public void update(Ventilator vtr) {
        currentSystemPressure = vtr.getLungSim().getCurrentDynamicPressureInLung();
    }
}

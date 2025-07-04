package com.mvsim.model.ventilator;

import com.mvsim.model.observer.Observer;

/**
 * Represents a pressure sensor in a ventilator. Pressure-sensing is faked here by having the sensor ask the lung profile directly how much pressure it is feeling. Note
 * as well that this assumes an idealized pt-vtr system that is closed and depends only on the patient.
 */
public class PressureSensor implements Observer {

    @Override
    public void update(Object observable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    
}

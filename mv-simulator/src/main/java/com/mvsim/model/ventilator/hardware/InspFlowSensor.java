package com.mvsim.model.ventilator.hardware;

import com.mvsim.model.observer.Observer;
import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.mode.VentilationMode;

/**
 * Represents an inspiratory flow sensor in a ventilator. Inspiratory flow
 * sensing is faked in a
 * flow-based mode asking the ventilator about the relevant settings. For
 * simplicity, assume that inspiratory flow is 0 during the expiratory phase.
 * TODO: determination of flow in a pressure-based mode
 */
public class InspFlowSensor implements Observer {

    private float currentInspiratoryFlow = 0;

    public float getLatestInspiratoryFlowReading() {
        return currentInspiratoryFlow;
    }

    @Override
    public void update(Ventilator vtr) {
        if (!vtr.getController().getActiveMode().getIsInInspiratoryPhase()) {
            currentInspiratoryFlow = 0;
        } else {
            currentInspiratoryFlow = vtr.getActuator().getLastVolumeBolusDelivered() / (VentilationMode.TICK_PERIOD_IN_MS / 1000f);
        }
    }

}

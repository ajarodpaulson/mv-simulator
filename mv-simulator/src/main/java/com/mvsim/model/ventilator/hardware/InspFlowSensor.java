package com.mvsim.model.ventilator.hardware;

import com.mvsim.model.observer.VentilatorObserver;
import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.mode.VentilationMode;

/**
 * Represents an inspiratory flow sensor in a ventilator. Inspiratory flow
 * sensing is faked in a flow-based mode asking the ventilator about the
 * relevant settings. For simplicity, assume that inspiratory flow is 0 during
 * the expiratory phase. Flowrate is in Units.Flowrate
 * TODO: determination of flow in a pressure-based mode
 */
public class InspFlowSensor implements VentilatorObserver {

    private float currentInspiratoryFlow = 0;

    /**
     * @return The latest inspiratory flowrate in Units.FLOWRATE
     */
    public float getLatestInspiratoryFlowReading() {
        return currentInspiratoryFlow;
    }

    @Override
    public void update(Ventilator vtr) {
        if (!vtr.getController().getActiveMode().getIsInInspiratoryPhase()) {
            currentInspiratoryFlow = 0;
        } else {
            currentInspiratoryFlow = ((vtr.getActuator().getLastVolumeBolusDelivered() / 1000f)
                    / (VentilationMode.TICK_PERIOD_IN_MS / 1000f)) * 60;
        }
    }

}

package com.mvsim.model.ventilator.hardware;

import com.mvsim.model.observer.VentilatorObserver;
import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.mode.VentilationMode;

public class ExpFlowSensor implements VentilatorObserver {

    private float currentExpiratoryFlow = 0;

    /**
     * 
     * @return The latest expiratory flow in Units.FLOWRATE
     */
    public float getCurrentExpiratoryFlow() {
        return currentExpiratoryFlow;
    }

    @Override
    public void update(Ventilator vtr) {
        if (vtr.getController().getActiveMode().getIsInInspiratoryPhase()) {
            currentExpiratoryFlow = 0;
        } else {
            currentExpiratoryFlow = -1 * (((vtr.getLungSim().getVolumeChangeInMls() / 1000f) / (VentilationMode.TICK_PERIOD_IN_MS / 1000f)) * 60);
        }
    }
    
}

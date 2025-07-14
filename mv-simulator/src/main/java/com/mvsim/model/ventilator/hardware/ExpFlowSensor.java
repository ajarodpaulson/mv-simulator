package com.mvsim.model.ventilator.hardware;

import com.mvsim.model.observer.Observer;
import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.mode.VentilationMode;

public class ExpFlowSensor implements Observer {

    private float currentExpiratoryFlow = 0;

    public float getCurrentExpiratoryFlow() {
        return currentExpiratoryFlow;
    }

    @Override
    public void update(Ventilator vtr) {
        if (vtr.getController().getActiveMode().getIsInInspiratoryPhase()) {
            currentExpiratoryFlow = 0;
        } else {
            currentExpiratoryFlow = vtr.getLungSim().getVolumeChange() / (VentilationMode.TICK_PERIOD_IN_MS / 1000f);
        }
    }
    
}

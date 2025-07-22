package com.mvsim.model.ventilator;

import com.mvsim.model.SimulationManager;

public class MostRecentTickData {
    private float currentSystemPressure;
    private float currentSystemFlowrate; 
    private float currentSystemVolume;
    private float currentSystemTime;

    public MostRecentTickData(SimulationManager simMgr) {
        update(simMgr);
    }

    public void update(SimulationManager simMgr) {
        this.currentSystemPressure = simMgr.getCurrentSystemPressure();
        this.currentSystemFlowrate = simMgr.getCurrentSystemFlowrate();
        this.currentSystemVolume = simMgr.getCurrentSystemVolumeChange();
        this.currentSystemTime = (simMgr.getVtrController().getTickPeriodInMs() / 1000f) * simMgr.getVtrController().getActiveMode().getTick();
    }

    public float getCurrentSystemPressure() {
        return currentSystemPressure;
    }

    public float getCurrentSystemFlowrate() {
        return currentSystemFlowrate;
    }

    public float getCurrentSystemVolume() {
        return currentSystemVolume;
    }

    public float getCurrentSystemTime() {
        return currentSystemTime;
    }
}

package com.mvsim.model;

import com.mvsim.model.lungsim.LungSim;
import com.mvsim.model.ventilator.Ventilator;

/**
 * Represents a manager for the lung simulator and mechanical ventilation
 * simulator complex. Uses the singleton pattern.
 */
public class SimulationManager {
    private Ventilator vtr;
    private LungSim lungSim;
    private static SimulationManager theManager;

    private SimulationManager() {
        this.vtr = new Ventilator();
    }

    /**
     * Gets instance of SimulationManager - creates it
     * if it doesn't already exist.
     * 
     * @return instance of SimulationManager
     */
    public static SimulationManager getInstance() {
        if (theManager == null) {
            theManager = new SimulationManager();
        }
        return theManager;
    }

    public Ventilator getVtr() {
        return vtr;
    }
    public void setVtr(Ventilator vtr) {
        this.vtr = vtr;
    }
    public LungSim getLungSim() {
        return lungSim;
    }
    public void setLungSim(LungSim lungSim) {
        this.lungSim = lungSim;
    }

    
}

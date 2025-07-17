package com.mvsim.model;

import com.mvsim.model.exception.ActiveModeNotSetException;
import com.mvsim.model.lungsim.LungSim;
import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.VentilatorController;

/**
 * Represents a manager for the lung simulator and mechanical ventilation
 * simulator complex. Uses the singleton pattern.
 */
public class SimulationManager {
    private VentilatorController vtrController;
    private final Ventilator vtr;
    private LungSim lungSim;
    private static SimulationManager theManager;

    private SimulationManager() {
        this.vtr = new Ventilator();
        this.vtrController = vtr.getController();
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

    public VentilatorController getVtrController() {
        return vtrController;
    }

    public void setVtr(VentilatorController vtrController) {
        this.vtrController = vtrController;
    }

    public LungSim getLungSim() {
        return lungSim;
    }

    /**
     * When the lung sim is set, the simulation manager connects it to the vtr
     * 
     * @param lungSim
     */
    public void setLungSim(LungSim lungSim) {
        this.lungSim = lungSim;
        this.vtr.setLungSim(lungSim);
    }

    /**
     * Starts the main simulation loop in a new thread.
     * This method is non-blocking.
     * 
     * @throws ActiveModeNotSetException if no ventilation mode is selected.
     */
    public void startSimulation() throws ActiveModeNotSetException {
        vtrController.enableVentilation();
        /*
         * we need to start the ventilation control loop on its own thread otherwise it
         * will block everything else from happening
         */
        Thread simulationThread = new Thread(() -> {
            while (vtrController.getIsVentilating()) {
                vtrController.tick();
                try {
                    // This controls the simulation speed
                    Thread.sleep(vtrController.getTickPeriodInMs());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        simulationThread.start();
    }

    // Add these methods for the UI to call
    public boolean isVentilating() {
        return vtrController.getIsVentilating();
    }

    public float getCurrentSystemPressure() {
        return vtrController.getCurrentSystemPressure();
    }

    public float getCurrentSystemFlowrate() {
        return vtrController.getCurrentSystemFlowrate();
    }

    public float getCurrentSystemVolumeChange() {
        return vtrController.getCurrentSystemVolumeChange();
    }

    public void stopSimulation() {
        vtrController.stopVentilation();
    }
}

package com.mvsim.model;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingUtilities;

import com.mvsim.model.exception.ActiveModeNotSetException;
import com.mvsim.model.lungsim.LungSim;
import com.mvsim.model.lungsim.LungSimSetting;
import com.mvsim.model.observer.Observable;
import com.mvsim.model.observer.SimMgrObserver;
import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.VentilatorController;
import com.mvsim.model.ventilator.metrics.MetricName;
// import com.mvsim.model.ventilator.metrics.MostRecentTickData;

/**
 * Represents a manager for the lung simulator and mechanical ventilation
 * simulator complex. Uses the singleton pattern.
 */
public class SimulationManager extends Observable implements ChangeListener {
    private VentilatorController vtrController;
    private Ventilator vtr;
    private LungSim lungSim;
    private static SimulationManager theManager;
    private Set<SimMgrObserver> observers;
    // private final MostRecentTickData mostRecentTickData;

    private SimulationManager() {
        this.vtr = new Ventilator();
        this.lungSim = new LungSim();
        vtr.setLungSim(lungSim);
        this.vtrController = vtr.getController();
        this.observers = new HashSet<>();
        // this.mostRecentTickData = new MostRecentTickData(this);
    }

    public void reset() {
        theManager = new SimulationManager();
    }

    public void addObserver(SimMgrObserver o) {
        observers.add(o);
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

    public LungSim getLungSim() {
        return lungSim;
    }

    public void setLungSimSetting(LungSimSetting setting, float value) {
        this.lungSim.getSettings().setSetting(setting, value);
    }

    /**
     * When the lung sim is set, the simulation manager connects it to the vtr
     * 
     * @param lungSim
     */
    public void configureLungSim(LungSimSetting name, Number value) {
        lungSim.setSetting(name, value);
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
                // Only notify - don't do the data collection work here
                notifyObservers();
                try {
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
        return vtrController.getMetrics().getCurrentSystemPressure();
    }

    public float getCurrentSystemFlowrate() {
        return vtrController.getMetrics().getCurrentSystemFlowrate();
    }

    public float getCurrentSystemVolumeChange() {
        return vtrController.getMetrics().getCurrentSystemVolumeChange();
    }

    public void stopSimulation() {
        vtrController.stopVentilation();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        
    }

    @Override
    public void notifyObservers() {
        SwingUtilities.invokeLater(() -> {
            updateMetrics();                                        
            for (SimMgrObserver o : observers) {
                o.update(vtrController.getMetrics());
            }
        });
    }

    private void updateMetrics() {
        // mostRecentTickData.update(this);
        vtrController.updateMetrics();
    }
}

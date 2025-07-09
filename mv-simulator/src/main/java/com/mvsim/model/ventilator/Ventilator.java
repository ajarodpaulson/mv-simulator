package com.mvsim.model.ventilator;

import java.util.Set;

import com.mvsim.model.lungsim.LungSim;
import com.mvsim.model.observer.Observable;

/**
 * Represents a ventilator that can have a single active mode and a single lung simulator connected. The ventilator uses the flow and pressure sensors in flow and pressure-based modes, 
 * respectively, to regulate its output.
 */
public class Ventilator extends Observable {
    private InspFlowSensor inspFlowSensor;
    private ExpFlowSensor expFlowSensor;
    private PressureSensor pressureSensor;
    private Actuator actuator;
    private VentilationMode activeMode;
    private MainController mainController;
    private LungSim lungSim; 
    private ModeTable modeTable;

    public Ventilator() {
        this.inspFlowSensor = new InspFlowSensor();
        this.expFlowSensor = new ExpFlowSensor();
        this.pressureSensor = new PressureSensor();
        this.actuator = new Actuator(this);
        this.modeTable = new ModeTable(this);
    }

    public VentilationMode getActiveMode() {
        return activeMode;
    }

    public void setActiveMode(VentilationMode activeMode) {
        this.activeMode = activeMode;
    }

    @Override
    public void notifyObservers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notifyObservers'");
    }

    /**
     * Starts ventilation by calling tick on the active mode.
     */
    public void startVentilation() {
        activeMode.tick();
    }

    /**
     * Stops ventilation by calling stop on the actuator and notifies observers that it has stopped.
     * TODO: performs any necessary cleanup (wrt the tick method?)
     */
    public void stopVentilation() {

    }

    public Set<ModeType> getAvailableModes() {
        return modeTable.getAvailableModes();
    }

    public ModeTable getModeTable() {
        return modeTable;
    }


}

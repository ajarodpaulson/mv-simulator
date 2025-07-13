package com.mvsim.model.ventilator;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mvsim.model.exception.ActiveModeNotSetException;
import com.mvsim.model.lungsim.LungSim;
import com.mvsim.model.observer.Observable;
import com.mvsim.model.observer.Observer;
import com.mvsim.model.ventilator.hardware.Actuator;
import com.mvsim.model.ventilator.hardware.ExpFlowSensor;
import com.mvsim.model.ventilator.hardware.InspFlowSensor;
import com.mvsim.model.ventilator.hardware.PressureSensor;
import com.mvsim.model.ventilator.mode.ModeTAG;
import com.mvsim.model.ventilator.mode.ModeTable;
import com.mvsim.model.ventilator.mode.VentilationMode;

/**
 * Represents a ventilator that can have a single active mode and a single lung
 * simulator connected. The ventilator uses the flow and pressure sensors in
 * flow and pressure-based modes,
 * respectively, to regulate its output.
 */
public class Ventilator extends Observable {

    private InspFlowSensor inspFlowSensor;
    private ExpFlowSensor expFlowSensor;
    private PressureSensor pressureSensor;
    private Actuator actuator;
    private VentilationMode<?> activeMode;
    private VentilatorController controller;
    private LungSim lungSim;

    public VentilatorController getController() {
        return controller;
    }

    public InspFlowSensor getInspFlowSensor() {
        return inspFlowSensor;
    }

    public ExpFlowSensor getExpFlowSensor() {
        return expFlowSensor;
    }

    public LungSim getLungSim() {
        return lungSim;
    }

    public void setLungSim(LungSim lungSim) {
        this.lungSim = lungSim;
    }

    private ModeTable modeTable;

    /*
     * XXX: I also think it would be fine to make this volatile. We do not have
     * multiple threads writing to this variable. The only thing we need to be
     * concerned with is the the display thread does not cache a local copy of this
     * variable. This might actually slow things down...?
     */
    private AtomicBoolean isVentilationEnabled;

    public boolean getIsVentilationEnabled() {
        return isVentilationEnabled.get();
    }

    public Ventilator() {
        this.inspFlowSensor = new InspFlowSensor();
        this.expFlowSensor = new ExpFlowSensor();
        this.pressureSensor = new PressureSensor();
        this.actuator = new Actuator(this);
        this.modeTable = new ModeTable(this);
        this.controller = new VentilatorController(this);
        isVentilationEnabled = new AtomicBoolean(false);

        this.addObserver(pressureSensor);
        this.addObserver(inspFlowSensor);
        this.addObserver(expFlowSensor);
    }

    public VentilationMode<?> getActiveMode() {
        return activeMode;
    }

    protected void setActiveMode(VentilationMode<?> activeMode) {
        this.activeMode = activeMode;
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    /**
     * Starts ventilation by calling tick on the active mode until something stops
     * ventilation. If the ventilator is already ventilating, do nothing.
     * 
     * @throws ActiveModeNotSetException
     */
    public void enableVentilation() throws ActiveModeNotSetException {
        if (getIsVentilationEnabled()) {
            return;
        }
        if (activeMode == null) {
            throw new ActiveModeNotSetException();
        }
        isVentilationEnabled.set(true);
    }

    /**
     * Stops ventilation by calling stop on the actuator and notifies observers that
     * it has stopped.
     * TODO: performs any necessary cleanup (wrt the tick method?)
     */
    public void disableVentilation() {
        isVentilationEnabled.set(false);
    }

    public Set<ModeTAG> getAvailableModes() {
        return modeTable.getAvailableModes();
    }

    public ModeTable getModeTable() {
        return modeTable;
    }

    public float getLatestFlowSensorReading() {
        return this.inspFlowSensor.getLatestInspiratoryFlowReading();
    }

    public Actuator getActuator() {
        return this.actuator;
    }

    public PressureSensor getPressureSensor() {
        return this.pressureSensor;
    }

    public void tick() {
        if (activeMode != null && isVentilationEnabled.get()) {
            activeMode.tick();
        }
    }

}

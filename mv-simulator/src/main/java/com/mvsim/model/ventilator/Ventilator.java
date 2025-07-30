package com.mvsim.model.ventilator;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mvsim.model.exception.ActiveModeNotSetException;
import com.mvsim.model.lungsim.LungSim;
import com.mvsim.model.observer.Observable;
import com.mvsim.model.observer.VentilatorObserver;
import com.mvsim.model.ventilator.hardware.Actuator;
import com.mvsim.model.ventilator.hardware.ExpFlowSensor;
import com.mvsim.model.ventilator.hardware.InspFlowSensor;
import com.mvsim.model.ventilator.hardware.PressureSensor;
import com.mvsim.model.ventilator.mode.BreathSequenceType;
import com.mvsim.model.ventilator.mode.ControlVariable;
import com.mvsim.model.ventilator.mode.ControlVariableType;
import com.mvsim.model.ventilator.mode.ModeTAG;
import com.mvsim.model.ventilator.mode.ModeTable;
import com.mvsim.model.ventilator.mode.TargetingSchemeType;
import com.mvsim.model.ventilator.mode.VentilationMode;
import com.mvsim.model.ventilator.settings.Peep;
import com.mvsim.model.ventilator.mode.VcCmvSetpoint;

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
    private final VentilatorController controller;
    private LungSim lungSim;
    private ModeTable modeTable;
    /*
     * XXX: I also think it would be fine to make this volatile. We do not have
     * multiple threads writing to this variable. The only thing we need to be
     * concerned with is the the display thread does not cache a local copy of this
     * variable. This might actually slow things down...?
     */
    private AtomicBoolean isVentilationEnabled;

    public Ventilator() {
        this.inspFlowSensor = new InspFlowSensor();
        this.expFlowSensor = new ExpFlowSensor();
        this.pressureSensor = new PressureSensor();
        this.actuator = new Actuator(this);
        this.modeTable = new ModeTable(this);
        this.activeMode = modeTable.getMode(new ModeTAG(ControlVariableType.VOLUME_CONTROL,
                BreathSequenceType.CONTINUOUS_MANDATORY_VENTILATION, TargetingSchemeType.SET_POINT)); // default mode
        isVentilationEnabled = new AtomicBoolean(false);
        this.controller = new VentilatorController(this);

        this.addObserver(pressureSensor);
        this.addObserver(inspFlowSensor);
        this.addObserver(expFlowSensor);
    }

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

    public boolean getIsVentilationEnabled() {
        return isVentilationEnabled.get();
    }

    public VentilationMode<?> getActiveMode() {
        return activeMode;
    }

    protected void setActiveMode(VentilationMode<?> activeMode) {
        this.activeMode = activeMode;
    }

    @Override
    public void notifyObservers() {
        for (VentilatorObserver o : observers) {
            o.update(this);
        }
    }

    /**
     * Enables ventilation checks to ensure there is an active mode and pressurizes
     * the system with the set PEEP. If the ventilator is already ventilating, do
     * nothing.
     * 
     * @throws ActiveModeNotSetException
     */
    /*
     * XXX: seems kind of pointless now that you are instantiating a ventilator with
     * a default active mode
     */
    public void enableVentilation() throws ActiveModeNotSetException {
        if (getIsVentilationEnabled()) {
            return;
        }
        if (activeMode == null) {
            throw new ActiveModeNotSetException();
        }
        isVentilationEnabled.set(true);
        actuator.instantlyPressurize(activeMode.getSetting(Peep.NAME).getValue().floatValue());

        notifyObservers();
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

    /**
     * Attempts to perform one control loop tick using the active ventilation mode.
     * 
     * @throws IllegalStateException if active mode null or was called while
     *                               ventilation disabled
     */
    public void tick() {
        if (activeMode == null) {
            throw new IllegalStateException("Active mode not set.");
        }
        if (!isVentilationEnabled.get()) {
            throw new IllegalStateException("Ventilation is not enabled.");
        }
        activeMode.tick();
    }

}

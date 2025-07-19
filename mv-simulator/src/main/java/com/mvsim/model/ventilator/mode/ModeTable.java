package com.mvsim.model.ventilator.mode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.mvsim.model.ventilator.Ventilator;

/**
 * Represents a table with the ventilator's available modes that should be used to construct a mode from a supplied ModeType.
 */
/*
 * XXX: factory pattern?
 */
public class ModeTable {

    private final Map<ModeTAG, VentilationMode<?>> modeTable = new LinkedHashMap<>();

    public void addMode(ModeTAG type, VentilationMode<?> mode) {
        modeTable.put(type, mode);
    }

    public VentilationMode<?> getMode(ModeTAG type) {
        return modeTable.get(type);
    }

    public ModeTable(Ventilator vtr) {
        addMode(
            new ModeTAG(ControlVariableType.VOLUME_CONTROL, BreathSequenceType.CONTINUOUS_MANDATORY_VENTILATION, TargetingSchemeType.SET_POINT),
            new VcCmvSetpoint(vtr)
        );
    }

    public Set<ModeTAG> getAvailableModes() {
        return Collections.unmodifiableSet(modeTable.keySet());
    }
}

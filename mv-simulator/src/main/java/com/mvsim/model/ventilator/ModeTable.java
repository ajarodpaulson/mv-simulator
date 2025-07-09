package com.mvsim.model.ventilator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a table with the ventilator's available modes that should be used to construct a mode from a supplied ModeType.
 */
public class ModeTable {

    private final Map<ModeType, VentilationMode<?>> modeTable = new LinkedHashMap<>();

    public void addMode(ModeType type, VentilationMode<?> mode) {
        modeTable.put(type, mode);
    }

    public VentilationMode<?> getMode(ModeType type) {
        return modeTable.get(type);
    }

    public ModeTable(Ventilator vtr) {
        addMode(
            new ModeType(ControlVariableType.VOLUME_CONTROL, BreathSequenceType.CONTINUOUS_MANDATORY_VENTILATION, TargetingSchemeType.SET_POINT),
            new VcCmvSetpoint(vtr)
        );
    }

    public Set<ModeType> getAvailableModes() {
        return modeTable.keySet();
    }
}

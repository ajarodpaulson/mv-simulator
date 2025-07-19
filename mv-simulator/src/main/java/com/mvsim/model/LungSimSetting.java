package com.mvsim.model;

public enum LungSimSetting {
    COMPLIANCE("Compliance", Units.COMPLIANCE, 100f),
    RESISTANCE("Resistance", Units.RESISTANCE, 1.0f);

    private final String name;
    private final Units units;
    private final Number defaultValue;

    LungSimSetting(String name, Units units, Number defaultValue) {
        this.name = name;
        this.units = units;
        this.defaultValue = defaultValue;
    }

    public Number getDefaultValue() {
        return defaultValue;
    }
    
}

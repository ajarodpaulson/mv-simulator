package com.mvsim.model.ventilator.mode;

public enum ControlVariableType {
    VOLUME_CONTROL("VC"),
    PRESSURE_CONTROL("PC");

    public String getName() {
        return name;
    }

    private final String name;

    ControlVariableType(String name) {
        this.name = name;
    }
}

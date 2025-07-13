package com.mvsim.model.ventilator.mode;

public enum TargetingSchemeType {
    SET_POINT("s");

    private final String name;

    public String getName() {
        return name;
    }

    TargetingSchemeType(String name) {
        this.name = name;
    }
}

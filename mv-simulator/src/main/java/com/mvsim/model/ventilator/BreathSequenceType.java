package com.mvsim.model.ventilator;

public enum BreathSequenceType {
    CONTINUOUS_MANDATORY_VENTILATION("CMV"),  // CMV
    INTERMITTENT_MANDATORY_VENTILATION("IMV"), // IMV
    CONTINUOUS_SPONTANEOUS_VENTILATION("CSV");  // CSV

    private final String name;

    public String getName() {
        return name;
    }

    BreathSequenceType(String name) {
        this.name = name;
    }
}

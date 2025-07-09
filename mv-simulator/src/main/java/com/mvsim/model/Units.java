package com.mvsim.model;

public enum Units {
    COMPLIANCE("ml/cmH2O"),
    RESISTANCE("cmH2O/L/s"),
    PERCENT("%"), 
    PRESSURE("cmH2O"),
    VOLUME("ml"), 
    RESP_RATE("breaths/min"), 
    TIME("s"),
    NOT_APPLICABLE("N/A");

    private final String notation;

    public String getNotation() {
        return notation;
    }

    Units(String notation) {
        this.notation = notation;
    }
}

package com.mvsim.model.ventilator.metrics;

public enum MetricName {
    PEAK_PRESSURE("Ppeak"),
    MEAN_PRESSURE("Pmean"),
    MEAN_PEEP("PEEP"),
    MEAN_RESP_RATE("RR"),
    EXHALED_MINUTE_VOLUME("MVe"),
    MEASURED_INHALED_TIDAL_VOLUME("VTi"),
    MEASURED_EXHALED_TIDAL_VOLUME("VTe"),
    DYNAMIC_COMPLIANCE("Cdyn");

    public String getDisplayName() {
        return displayName;
    }

    private final String displayName;

    MetricName(String displayName) {
        this.displayName = displayName;
    }
}

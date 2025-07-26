package com.mvsim.model.ventilator.metrics;

import com.mvsim.model.Units;

public class Metric {

    float value;
    Units units;

    Metric(float value, Units units) {
        this.value = value;
        this.units = units;
    }

    public float getValue() {
        return value;
    }

    public Units getUnits() {
        return units;
    }
}

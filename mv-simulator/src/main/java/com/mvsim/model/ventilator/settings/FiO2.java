package com.mvsim.model.ventilator.settings;

import com.mvsim.model.Units;

public class FiO2 extends Setting {

    public static final String NAME = "FiO2";
    public static final Units UNITS = Units.PERCENT;

    FiO2(Number value) {
        super(FiO2.NAME, FiO2.UNITS, value);
    }
}

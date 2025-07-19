package com.mvsim.model.ventilator.settings;

import com.mvsim.model.Setting;
import com.mvsim.model.Units;

public class FiO2 extends Setting {

    public static final String NAME = "FiO2";
    public static final Units UNITS = Units.PERCENT;
    public static final float DEFAULT_VALUE = 0.5f;

    FiO2(Number value) {
        super(FiO2.NAME, FiO2.UNITS, value);
    }

    public FiO2() {
        this(DEFAULT_VALUE);
    }
}

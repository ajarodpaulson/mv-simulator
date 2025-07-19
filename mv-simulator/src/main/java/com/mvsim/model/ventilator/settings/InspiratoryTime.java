package com.mvsim.model.ventilator.settings;

import com.mvsim.model.Setting;
import com.mvsim.model.Units;

public class InspiratoryTime extends Setting {

    public static final String NAME = "Inspiratory Time";
    public static final Units UNITS = Units.TIME;
    public static final float DEFAULT_VALUE = 1.0f;

    // TODO: the value of inspiratory time needs to be a multiple of
    // VentilationMode.TICK_PERIOD_IN_MS, need to enforce this
    public InspiratoryTime(Number value) {
        super(InspiratoryTime.NAME, InspiratoryTime.UNITS, value);
    }

    public InspiratoryTime() {
        this(DEFAULT_VALUE);
    }

}

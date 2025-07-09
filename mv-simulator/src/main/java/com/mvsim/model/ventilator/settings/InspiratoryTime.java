package com.mvsim.model.ventilator.settings;

import com.mvsim.model.Units;

public class InspiratoryTime extends Setting {

    public static final String NAME = "Inspiratory Time";
    public static final Units UNITS = Units.TIME;

    public InspiratoryTime(Number value) {
        super(InspiratoryTime.NAME, InspiratoryTime.UNITS, value);
    }
    
}

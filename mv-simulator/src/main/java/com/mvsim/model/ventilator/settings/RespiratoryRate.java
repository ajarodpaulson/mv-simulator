package com.mvsim.model.ventilator.settings;

import com.mvsim.model.Units;

public class RespiratoryRate extends Setting {

    public static final String NAME = "Respiratory Rate";
    public static final Units UNITS = Units.RESP_RATE;

    public RespiratoryRate(Number value) {
        super(RespiratoryRate.NAME, RespiratoryRate.UNITS, value);
    }
}

package com.mvsim.model.ventilator.settings;

import com.mvsim.model.Units;

public class Peep extends Setting {
    public static final String NAME = "PEEP";
    public static final Units UNITS = Units.PRESSURE;

    Peep(Number value) {
        super(Peep.NAME, Peep.UNITS, value);
    }
}

package com.mvsim.model.ventilator.settings;

import com.mvsim.model.Units;

public class Peep extends Setting {
    public static final String NAME = "PEEP";
    public static final Units UNITS = Units.PRESSURE;
    public static final float DEFAULT_VALUE = 5.0f;

    Peep(Number value) {
        super(Peep.NAME, Peep.UNITS, value, 0f, 100f, 0.5f);
    }

    public Peep() {
        this(DEFAULT_VALUE);
    }
}

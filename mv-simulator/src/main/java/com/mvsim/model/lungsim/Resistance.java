package com.mvsim.model.lungsim;

import com.mvsim.model.Setting;
import com.mvsim.model.Units;

public class Resistance extends Setting {
   public static final String NAME = "Resistance";
    public static final Units UNITS = Units.RESISTANCE;
    public static final float DEFAULT_VALUE = 1.0f;

    public Resistance(Number value) {
        super(Resistance.NAME, Resistance.UNITS, value);
    }

    public Resistance() {
        this(DEFAULT_VALUE);
    }
}

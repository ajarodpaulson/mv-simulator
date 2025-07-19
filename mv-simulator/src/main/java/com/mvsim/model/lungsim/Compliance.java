package com.mvsim.model.lungsim;

import com.mvsim.model.Setting;
import com.mvsim.model.Units;

public class Compliance extends Setting {
   public static final String NAME = "Compliance";
    public static final Units UNITS = Units.COMPLIANCE;
    public static final float DEFAULT_VALUE = 100f;

    public Compliance(Number value) {
        super(Compliance.NAME, Compliance.UNITS, value);
    }

    public Compliance() {
        this(DEFAULT_VALUE);
    }
}

package com.mvsim.model.ventilator.settings;

import com.mvsim.model.Units;

public class TidalVolume extends Setting {

    public static final String NAME = "Tidal Volume";
    public static final Units UNITS = Units.VOLUME;
    public static final float DEFAULT_VALUE = 350f;

    public TidalVolume(Number value) {
        super(TidalVolume.NAME, TidalVolume.UNITS, value, 0f, 5000f, 10f);
    }

    public TidalVolume() {
        this(DEFAULT_VALUE);
    }
    
}

package com.mvsim.model.lungsim;

import com.mvsim.model.Units;
import com.mvsim.model.ventilator.settings.NewSetting;

/*
 * Why is this an enum?
 */
public enum LungSimSetting implements NewSetting {
    COMPLIANCE("Compliance", Units.COMPLIANCE, 100f, 0f, 1000f, 1f),
    RESISTANCE("Resistance", Units.RESISTANCE, 1.0f, 0f, 100f, 0.1f);

    private final String name;
    private final Units units;
    private final Number defaultValue;
    private final float minValue;
    private final float maxValue;
    private final float stepSize;

    LungSimSetting(String name, Units units, Number defaultValue, float minValue, float maxValue, float stepSize) {
        this.name = name;
        this.units = units;
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = stepSize;
    }

    // TODO: must fix later after you refactor ventilator settings to use the same approach
    public static LungSimSetting getLungSimSetting(String name) {
        for (LungSimSetting setting : LungSimSetting.values()) {
            if (setting.getName().equalsIgnoreCase(name)) {
            return setting;
            }
        }
        return null;
    }

    @Override
    public Number getDefaultValue() {
        return defaultValue;
    }

    @Override
    public float getMin() {
        return minValue;
    }

    @Override
    public float getStepSize() {
        return stepSize;
    }

    @Override
    public float getMax() {
        return maxValue;
    }

    @Override
    public String getName() {
        return name;
    }
    
}

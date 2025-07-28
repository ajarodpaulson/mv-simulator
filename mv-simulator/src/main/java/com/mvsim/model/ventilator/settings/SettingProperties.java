package com.mvsim.model.ventilator.settings;

public interface SettingProperties {

    Number getDefaultValue();

    float getMin();

    float getStepSize();

    float getMax();

    String getName();

}
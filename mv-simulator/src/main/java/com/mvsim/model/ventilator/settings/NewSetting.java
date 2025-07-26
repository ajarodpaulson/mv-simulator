package com.mvsim.model.ventilator.settings;

public interface NewSetting {

    Number getDefaultValue();

    float getMin();

    float getStepSize();

    float getMax();

    String getName();

}
package com.mvsim.model;

public interface NewSetting {

    Number getDefaultValue();

    float getMin();

    float getStepSize();

    float getMax();

    String getName();

}
package com.mvsim.model.lungsim;

import com.mvsim.model.ventilator.mode.VentilationMode;

/**
 * Represents a lung simulator that can be connected to a ventilator.
 * 
 * INVARIANT: The current lung volume and current lung pressure must always
 * correspond with one another using the lung's intrinsic compliance
 */
/*
 * TODO: any utility in adding the builder pattern here?
 */
public class LungSim {
    private static final float TOL = 0.00001f;
    private LungSimSettings lungSimSettings;
    private float compliance; // XXX get rid of this eventually? not sure. much easier to refer to like this
    private float resistance;
    private BreathingPattern breathingPattern;
    private float currentVolumeInLung = 0.0f;
    private float currentStaticPressureInLung = 0.0f;
    private float currentDynamicPressureInLung = 0.0f;
    private float prevVtrPressure = Float.MIN_VALUE;
    private float currentPressureChangePerTick = 0;
    private float volumeChange;

    private boolean pressureAndVolumeInLungAreSame() {
        return (currentStaticPressureInLung >= currentVolumeInLung / compliance - TOL ||  currentStaticPressureInLung <= currentVolumeInLung / compliance + TOL);
    }

    public float getVolumeChange() {
        return volumeChange;
    }

    public void setCurrentDynamicPressureInLung(float flowRate) {
        currentDynamicPressureInLung = currentStaticPressureInLung + flowRate * (resistance / 1000f);
    }

     public float getCurrentDynamicPressureInLung() {
        return currentDynamicPressureInLung;
    }

    public LungSim() {
        this.lungSimSettings = new LungSimSettings();
        this.compliance = lungSimSettings.getSetting(LungSimSetting.COMPLIANCE).floatValue();
        this.resistance = lungSimSettings.getSetting(LungSimSetting.RESISTANCE).floatValue();
        this.breathingPattern = null;
    }

    protected float getTimeConstant() {
        return compliance * (resistance / 1000);
    }

    /**
     * If the vtr pressure has not changed from last time, do not recalculate the
     * pressure change per tick. Otherwise, calculate the pressure change using the
     * time constant and assuming that full equilibration takes 5 time constants.
     * 
     * @param currentVtrPressure
     * @return The pressure change per tick
     */
    protected float getPressureChangePerTick(float currentVtrPressure) {
        if (currentVtrPressure == prevVtrPressure) {
            return currentPressureChangePerTick;
        } else {
            currentPressureChangePerTick = Math.abs(currentStaticPressureInLung - currentVtrPressure)
                    * ((VentilationMode.TICK_PERIOD_IN_MS / 1000f) / (5 * getTimeConstant()));
            return currentPressureChangePerTick;
        }
    }

    public BreathingPattern getBreathingPattern() {
        return breathingPattern;
    }

    public void setBreathingPattern(BreathingPattern breathingPattern) {
        this.breathingPattern = breathingPattern;
    }

    public LungSimSettings getSettings() {
        return lungSimSettings;
    }

    /**
     * Adds volume to the lung and updates resulting pressure based on this
     * lung's compliance. Note that this method works differently than
     * equilibratePressure because the actuator is only delivering the volume for
     * one tick of the ventilator
     * 
     * @param volumeToAdd Volume in mL
     */
    public void addVolume(float volumeToAdd) {
        assert pressureAndVolumeInLungAreSame();
        currentVolumeInLung += volumeToAdd;
        updateCurrentPressure(volumeToAdd);
        assert pressureAndVolumeInLungAreSame();
    }

    private void updateCurrentPressure(float volumeToAdd) {
        currentStaticPressureInLung = currentVolumeInLung / compliance;
        setCurrentDynamicPressureInLung(volumeToAdd / (VentilationMode.TICK_PERIOD_IN_MS / 1000f));
        assert pressureAndVolumeInLungAreSame();
    }

    /**
     * Gets the current volume in the lung
     * 
     * @return Current volume in mL
     */
    public float getCurrentVolumeInLung() {
        return currentVolumeInLung;
    }

    /**
     * If pressure is the same do nothing. Otherwise, adjust the pressure in the
     * lung using getPressureChangePerTick()
     * 
     * @param vtrPressure Pressure in cmH20
     */
    public void equilibratePressure(float vtrPressure) {
        assert pressureAndVolumeInLungAreSame();
        if (vtrPressure == currentStaticPressureInLung) { // XXX: add some tolerance here?
            return;
        } else if (vtrPressure > currentStaticPressureInLung) {
            currentStaticPressureInLung += getPressureChangePerTick(vtrPressure);
        } else {
            currentStaticPressureInLung -= getPressureChangePerTick(vtrPressure);
        }
        updateCurrentVolume();
        setCurrentDynamicPressureInLung(volumeChange / (VentilationMode.TICK_PERIOD_IN_MS / 1000f));
        assert pressureAndVolumeInLungAreSame();
    }

    private void updateCurrentVolume() {
        float newVolume = currentStaticPressureInLung * compliance;
        this.volumeChange = Math.abs(newVolume - currentVolumeInLung);
        currentVolumeInLung = newVolume;
        assert pressureAndVolumeInLungAreSame();
    }

    /**
     * Gets the current pressure in the lung
     * 
     * @return Current pressure in cmH2O
     */
    public float getCurrentStaticPressureInLung() {
        return currentStaticPressureInLung;
    }

    public void setSetting(LungSimSetting name, Number value) {
        lungSimSettings.setSetting(name, value);
        
        // Update cached values XXX do I want to get rid of this later?
        switch (name) {
            case COMPLIANCE:
                this.compliance = value.floatValue();
                break;
            case RESISTANCE:
                this.resistance = value.floatValue();
                break;
        }
    }
}

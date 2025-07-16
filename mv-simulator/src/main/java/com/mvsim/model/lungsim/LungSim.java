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
    private float compliance;
    private float resistance;
    private BreathingPattern breathingPattern;
    private float currentVolumeInLung = 0.0f;
    private float currentPressureInLung = 0.0f;
    private float prevVtrPressure = Float.MIN_VALUE;
    private float currentPressureChangePerTick = 0;
    private float volumeChange;

    private boolean pressureAndVolumeInLungAreSame() {
        return (currentPressureInLung >= currentVolumeInLung / compliance - TOL ||  currentPressureInLung <= currentVolumeInLung / compliance + TOL);
    }

    public float getVolumeChange() {
        return volumeChange;
    }

    public LungSim(float compliance, float resistance) {
        this.compliance = compliance;
        this.resistance = resistance;
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
            currentPressureChangePerTick = Math.abs(currentPressureInLung - currentVtrPressure)
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

    public float getCompliance() {
        return compliance;
    }

    public void setCompliance(float compliance) {
        this.compliance = compliance;
    }

    public float getResistance() {
        return resistance;
    }

    public void setResistance(float resistance) {
        this.resistance = resistance;
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
        updateCurrentPressure();
        assert pressureAndVolumeInLungAreSame();
    }

    private void updateCurrentPressure() {
        currentPressureInLung = currentVolumeInLung / compliance;
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
        if (vtrPressure == currentPressureInLung) { // XXX: add some tolerance here?
            return;
        } else if (vtrPressure > currentPressureInLung) {
            currentPressureInLung += getPressureChangePerTick(vtrPressure);
        } else {
            currentPressureInLung -= getPressureChangePerTick(vtrPressure);
        }
        updateCurrentVolume();
        assert pressureAndVolumeInLungAreSame();
    }

    private void updateCurrentVolume() {
        float newVolume = currentPressureInLung * compliance;
        this.volumeChange = Math.abs(newVolume - currentVolumeInLung);
        currentVolumeInLung = newVolume;
        assert pressureAndVolumeInLungAreSame();
    }

    /**
     * Gets the current pressure in the lung
     * 
     * @return Current pressure in cmH2O
     */
    public float getCurrentPressureInLung() {
        return currentPressureInLung;
    }
}

package com.mvsim.model.lungsim;

import com.mvsim.model.Utilities;
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
    private float prevTargetPressure = Float.MIN_VALUE;
    private float currentPressureChangePerTick = 0;
    private float volumeChangeInMls;

    private boolean pressureAbovePeepAndVolumeInLungAreCorresponding() {
        return (currentStaticPressureInLung >= currentVolumeInLung / compliance - TOL
                || currentStaticPressureInLung <= currentVolumeInLung / compliance + TOL);
    }

    public float getVolumeChangeInMls() {
        return volumeChangeInMls;
    }

    public void setCurrentDynamicPressureInLung(float flowrateInLPerS) {
        currentDynamicPressureInLung = currentStaticPressureInLung + flowrateInLPerS * (resistance);
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
     * @param currentTargetPressure
     * @return The pressure change per tick
     */
    protected float getPressureChangePerTick(float currentTargetPressure) {
        if (Utilities.equalWithinTolerance(currentTargetPressure, prevTargetPressure)) {
            return currentPressureChangePerTick;
        } else {
            prevTargetPressure = currentTargetPressure;
            currentPressureChangePerTick = Math.abs(currentStaticPressureInLung - currentTargetPressure)
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
        assert pressureAbovePeepAndVolumeInLungAreCorresponding();
        currentVolumeInLung += volumeToAdd;
        updateCurrentPressure(volumeToAdd);
        assert pressureAbovePeepAndVolumeInLungAreCorresponding();
    }

    private void updateCurrentPressure(float volumeToAdd) {
        currentStaticPressureInLung += volumeToAdd / compliance;
        setCurrentDynamicPressureInLung((volumeToAdd / 1000f) / (VentilationMode.TICK_PERIOD_IN_MS / 1000f));
        assert pressureAbovePeepAndVolumeInLungAreCorresponding();
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
     * This method will adjust the pressure in the lung using
     * getPressureChangePerTick() if the supplied pressure is different from that in
     * the lung, otherwise, the method does nothing.
     * 
     * @param targetPressure Ventilator pressure in cmH20
     */
    public void equilibratePressure(float targetPressure) {
        assert pressureAbovePeepAndVolumeInLungAreCorresponding();
        if (Utilities.equalWithinTolerance(targetPressure, currentStaticPressureInLung)) { 
            volumeChangeInMls = 0f;
            currentDynamicPressureInLung = currentStaticPressureInLung; // since there would be no further flow at this point
            return;
        } else if (targetPressure > currentStaticPressureInLung) {
            currentStaticPressureInLung += getPressureChangePerTick(targetPressure);
        } else {
            currentStaticPressureInLung -= getPressureChangePerTick(targetPressure);
        }
        updateCurrentVolume();
        setCurrentDynamicPressureInLung((volumeChangeInMls / 1000f) / (VentilationMode.TICK_PERIOD_IN_MS / 1000f));
        assert pressureAbovePeepAndVolumeInLungAreCorresponding();
    }

    private void updateCurrentVolume() {
        float newVolume = currentStaticPressureInLung * compliance;
        this.volumeChangeInMls = Math.abs(newVolume - currentVolumeInLung);
        currentVolumeInLung = newVolume;
        assert pressureAbovePeepAndVolumeInLungAreCorresponding();
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

    /**
     * A hack for adding PEEP to the lung once the ventilator is first started.
     * "Instantaneously" increases the pressure in the lung to that supplied.
     * 
     * @param peep the set PEEP from the active mode
     */
    /*
     * When peep is decreased, this may/will cause an issue as adding PEEP did not
     * increase the lung volume
     */
    public void instantlyPressurize(float peep) {
        currentStaticPressureInLung += peep;
        currentDynamicPressureInLung += peep;
        updateCurrentVolume();
    }
}

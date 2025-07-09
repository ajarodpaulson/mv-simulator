package com.mvsim.model.lungsim;

/**
 * Represents a lung simulator that can be connected to a ventilator.
 */
/*
 * TODO: any utility in adding the builder pattern here?
 */
public class LungSim {
    private float compliance;
    private float resistance;
    private BreathingPattern breathingPattern;
    private float currentVolume;
    private float currentPressure;

    public LungSim(float compliance, float resistance) {
        this.compliance = compliance;
        this.resistance = resistance;
        this.currentVolume = 0.0f;
        this.currentPressure = 0.0f;
        this.breathingPattern = null;
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
     * lung's compliance
     * @param volumeToAdd Volume in mL
     */
    public void addVolume(float volumeToAdd) {
        // TODO
    }

    /**
     * Gets the current volume in the lung
     * @return Current volume in mL
     */
    public float getCurrentVolume() {
        return currentVolume;
    }

    /**
     * Adds pressure to the lung and updates resulting volume based on this 
     * lung's compliance
     * @param pressureToAdd Pressure in cmH20
     */
    public void addPressure(float pressureToAdd) {
        // TODO
    }

    /**
     * Gets the current pressure in the lung
     * @return Current pressure in cmH2O
     */
    public float getCurrentPressure() {
        return currentPressure;
    }
}

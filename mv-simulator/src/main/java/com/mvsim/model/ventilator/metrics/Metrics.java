package com.mvsim.model.ventilator.metrics;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mvsim.model.Units;
import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.mode.VentilationMode;

/**
 * Represents a class that contains all ventilation metrics. This class is
 * updated after each control loop tick.
 */
public class Metrics {

    private Ventilator vtr;
    private int breathCycles = 0;
    private float peakPressure;
    private float nextPeakPressure;
    private boolean wasLastBreathPhaseInspiratory = false;
    private float nextMeanPressure;
    private float meanPressure;
    private float currentSystemPressure;
    private Map<MetricName, Metric> metricsMap;

    public Metrics(Ventilator vtr) {
        this.vtr = vtr;
        metricsMap = new LinkedHashMap<>();
        updateMetricsMap();
    }

    private void updateMetricsMap() {
        metricsMap.put(MetricName.PEAK_PRESSURE, new Metric(getPeakPressure(), Units.PRESSURE));
        metricsMap.put(MetricName.MEAN_PRESSURE, new Metric(getMeanPressure(), Units.PRESSURE));
        metricsMap.put(MetricName.MEAN_PEEP, new Metric(getMeanPeep(), Units.PRESSURE));
        metricsMap.put(MetricName.MEAN_RESP_RATE, new Metric(getMeanRespRate(), Units.RESP_RATE));
        metricsMap.put(MetricName.EXHALED_MINUTE_VOLUME, new Metric(getMinuteVolume(), Units.FLOWRATE));
        metricsMap.put(MetricName.MEASURED_INHALED_TIDAL_VOLUME, new Metric(getMeasuredInhaledTidalVolume(), Units.VOLUME));
        metricsMap.put(MetricName.MEASURED_EXHALED_TIDAL_VOLUME, new Metric(getMeasuredExhaledTidalVolume(), Units.VOLUME));
        metricsMap.put(MetricName.DYNAMIC_COMPLIANCE, new Metric(getDynamicCompliance(), Units.COMPLIANCE));
    }

    public Set<Entry<MetricName, Metric>> getAllMetrics() {
        return metricsMap.entrySet();
    }
     
    public float getCurrentSystemPressure() {
        return currentSystemPressure;
    }

    private float currentSystemFlowrate; 
    private float currentSystemVolumeChange;
    private float currentSystemTime;

    public float getCurrentSystemTime() {
        return currentSystemTime;
    }

    /**
     * @return The highest pressure from the most recent inspiratory phase
     *         The method should not compute a new peak pressure during the
     *         inspiratory phase. A new peak pressure should be provided once the
     *         breath phase transitions from inspiration to expiration, and for the
     *         remainder of the expiratory phase the method should return a stored value         
     */
    public float getPeakPressure() {
        if (!vtr.getActiveMode().getIsInInspiratoryPhase()) {
            if (wasLastBreathPhaseInspiratory) {
               peakPressure = nextPeakPressure;
            }
        } else {
            nextPeakPressure = Math.max(peakPressure, vtr.getPressureSensor().getCurrentSystemPressure());
        }
        return peakPressure;
    }

    /**
     * @return The sum of (pressure * tick period) / total breath cycle time for the
     *         most recent breath cycle
     */
    public float getMeanPressure() {
        if (vtr.getActiveMode().getIsInInspiratoryPhase()) {
            if (!wasLastBreathPhaseInspiratory) {
                meanPressure = nextMeanPressure;
            }
        } else {
            nextMeanPressure += (vtr.getPressureSensor().getCurrentSystemPressure() * vtr.getActiveMode().getTickPeriod());
        }
        return meanPressure;
    }

    /**
     * @return The pressure value during the expiratory phase only
     */
    public float getMeanPeep() {
        return 0;
    }

    /**
     * 
     * @return The number of breaths delivered over the last 5s * 12
     */
    public float getMeanRespRate() {
        return 0;
    }

    /**
     * 
     * @return The number of breaths delivered over the last 10s * the average tidal
     *         volume over the last 10s
     */
    public float getMinuteVolume() {
        return 0;
    }

    /**
     * 
     * @return the inhaled tidal volume from the most recent inspiratory phase
     */
    public float getMeasuredInhaledTidalVolume() {
        return 0;
    }

    /**
     * 
     * @return the inhaled tidal volume from the most recent inspiratory phase
     */
    public float getMeasuredExhaledTidalVolume() {
        return 0;
    }

    /**
     * 
     * @return the measured exhaled tidal volume from the last breath divided by the
     *         peak inspiratory pressure minus the PEEP
     */
    public float getDynamicCompliance() {
        return 0;
    }

    public float getCurrentSystemFlowrate() {
        if (vtr.getActiveMode().getIsInInspiratoryPhase()) {
            return vtr.getInspFlowSensor().getLatestInspiratoryFlowReading();
        } else {
            return vtr.getExpFlowSensor().getCurrentExpiratoryFlow();
        }
    }

    public float getCurrentSystemVolumeChange() {
        if (vtr.getActiveMode().getIsInInspiratoryPhase()) {
            currentSystemVolumeChange += vtr.getInspFlowSensor().getLatestInspiratoryFlowReading()
                    * (VentilationMode.TICK_PERIOD_IN_MS / 1000f);
            return currentSystemVolumeChange;
        } else {
            currentSystemVolumeChange -= vtr.getExpFlowSensor().getCurrentExpiratoryFlow()
                    * (VentilationMode.TICK_PERIOD_IN_MS / 1000f);
            return currentSystemVolumeChange;
        }
    }

    public void update() {
        this.currentSystemPressure = vtr.getPressureSensor().getCurrentSystemPressure();
        this.currentSystemFlowrate = getCurrentSystemFlowrate();
        this.currentSystemVolumeChange = getCurrentSystemVolumeChange();
        this.currentSystemTime = (vtr.getActiveMode().getTickPeriod() / 1000f) * vtr.getActiveMode().getTickCounter();
        updateMetricsMap();
        wasLastBreathPhaseInspiratory = vtr.getActiveMode().getIsInInspiratoryPhase();
    }

    public Metric get(MetricName metricName) {
        return metricsMap.get(metricName);
    }
}

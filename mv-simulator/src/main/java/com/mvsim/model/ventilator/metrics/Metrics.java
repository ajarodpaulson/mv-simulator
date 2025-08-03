package com.mvsim.model.ventilator.metrics;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mvsim.model.Units;
import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.VentilatorController;
import com.mvsim.model.ventilator.mode.VentilationMode;

/**
 * Represents a class that contains all ventilation metrics. This class is
 * updated after each control loop tick.
 * 
 * The metricsMap is the authoritative structure that holds the values that will
 * be shown to the user. However, additional instance variables are required for
 * some metrics to support calculating of the next output metric
 */
public class Metrics {

    private Ventilator vtr;
    private int breathCycles = 0;
    private float peakPressure;
    private float nextPeakPressure;
    private boolean didPhaseTransitionFromExpToInsp = false;
    private float sumPressureTimeIntegrals;
    private float meanPressure;
    private float currentSystemPressure; // TODO: add to map
    private float currentSystemFlowrate; // TODO: add to map
    private float currentSystemVolumeChange = 0.0f; // TODO: add to map
    private float currentSystemTime; // TODO: add to map
    private Map<MetricName, Metric> metricsMap;
    private float breathCycleTime;
    private VentilatorController controller;
    private boolean isPreviousBreathPhaseInspiratory;
    private boolean didPhaseTransitionFromInspToExp;

    public Metrics(Ventilator vtr, VentilatorController controller) {
        this.vtr = vtr;
        this.controller = controller;
        metricsMap = new LinkedHashMap<>();
        initMetricsMap();
    }

    private void initMetricsMap() {
        metricsMap.put(MetricName.PEAK_PRESSURE, new Metric(0f, Units.PRESSURE));
        metricsMap.put(MetricName.MEAN_PRESSURE, new Metric(0f, Units.PRESSURE));
        metricsMap.put(MetricName.MEAN_PEEP, new Metric(0f, Units.PRESSURE));
        metricsMap.put(MetricName.MEAN_RESP_RATE, new Metric(0f, Units.RESP_RATE));
        metricsMap.put(MetricName.EXHALED_MINUTE_VOLUME, new Metric(0f,
                Units.FLOWRATE));
        metricsMap.put(MetricName.MEASURED_INHALED_TIDAL_VOLUME,
                new Metric(0f, Units.VOLUME));
        metricsMap.put(MetricName.MEASURED_EXHALED_TIDAL_VOLUME,
                new Metric(0f, Units.VOLUME));
        metricsMap.put(MetricName.DYNAMIC_COMPLIANCE, new Metric(0f,
                Units.COMPLIANCE));
    }

    private void updateMetricsMap() {
        updatePeakPressure();
        updateMeanPressure();
        updateMeanPeep();
        updateMeanRespRate();
        updateMinuteVolume();
        updateMeasuredExhaledTidalVolume();
        updateMeasuredInhaledTidalVolume();
        updateDynamicCompliance();
    }

    public Set<Entry<MetricName, Metric>> getAllMetrics() {
        return metricsMap.entrySet();
    }

    public float getCurrentSystemPressure() {
        return currentSystemPressure;
    }

    public float getCurrentSystemTime() {
        return currentSystemTime;
    }

    /**
     * @return The highest pressure from the most recent inspiratory phase
     *         The method should not compute a new peak pressure during the
     *         inspiratory phase. A new peak pressure should be provided once the
     *         breath phase transitions from inspiration to expiration, and for the
     *         remainder of the expiratory phase the method should return a stored
     *         value
     */
    public void updatePeakPressure() {
        if (!vtr.getActiveMode().getIsInInspiratoryPhase() && didPhaseTransitionFromInspToExp) {
            setMetricValue(MetricName.PEAK_PRESSURE, nextPeakPressure);
        } else {
            nextPeakPressure = Math.max(metricsMap.get(MetricName.PEAK_PRESSURE).getValue(),
                    vtr.getPressureSensor().getCurrentSystemPressure());
        }
    }

    /**
     * @return The sum of (pressure * tick period) / total breath cycle time for the
     *         most recent breath cycle
     */
    public void updateMeanPressure() {
        if (vtr.getActiveMode().getIsInInspiratoryPhase() && didPhaseTransitionFromExpToInsp) {
            float breathCycleTime = controller.getTimeInPreviousBreathCycle();
            setMetricValue(MetricName.MEAN_PRESSURE,
                    breathCycleTime != 0 ? sumPressureTimeIntegrals / breathCycleTime : 0);
            sumPressureTimeIntegrals = 0;
        } else {
            sumPressureTimeIntegrals += (vtr.getPressureSensor().getCurrentSystemPressure()
                    * (vtr.getActiveMode().getTickPeriod() / 1000f));
        }
    }

    /**
     * @return The pressure value during the expiratory phase only
     */
    public float updateMeanPeep() {
        return 0;
    }

    /**
     * 
     * @return The number of breaths delivered over the last 5s * 12
     */
    public float updateMeanRespRate() {
        return 0;
    }

    /**
     * 
     * @return The number of breaths delivered over the last 10s * the average tidal
     *         volume over the last 10s
     */
    public float updateMinuteVolume() {
        return 0;
    }

    /**
     * 
     * @return the inhaled tidal volume from the most recent inspiratory phase
     */
    public float updateMeasuredInhaledTidalVolume() {
        return 0;
    }

    /**
     * 
     * @return the inhaled tidal volume from the most recent inspiratory phase
     */
    public float updateMeasuredExhaledTidalVolume() {
        return 0;
    }

    /**
     * 
     * @return the measured exhaled tidal volume from the last breath divided by the
     *         peak inspiratory pressure minus the PEEP
     */
    public float updateDynamicCompliance() {
        return 0;
    }

    public float getCurrentSystemFlowrate() {
        if (vtr.getActiveMode().getIsInInspiratoryPhase()) {
            return vtr.getInspFlowSensor().getLatestInspiratoryFlowReading();
        } else {
            return vtr.getExpFlowSensor().getCurrentExpiratoryFlow();
        }
    }

    public void updateCurrentSystemVolumeChange() {
        float cf = 1000f / 60f;
        if (vtr.getActiveMode().getIsInInspiratoryPhase()) {
            currentSystemVolumeChange += (vtr.getInspFlowSensor().getLatestInspiratoryFlowReading() * cf)
                    * (VentilationMode.TICK_PERIOD_IN_MS / 1000f);
        } else {
            currentSystemVolumeChange += (vtr.getExpFlowSensor().getCurrentExpiratoryFlow() * cf)
                    * (VentilationMode.TICK_PERIOD_IN_MS / 1000f);
        }
    }

    public float getCurrentSystemVolumeChange() {
        return currentSystemVolumeChange;
    }

    public void update() {
        this.currentSystemPressure = vtr.getPressureSensor().getCurrentSystemPressure();
        this.currentSystemFlowrate = getCurrentSystemFlowrate();
        updateCurrentSystemVolumeChange();
        this.currentSystemTime = controller.getVentilationTimeForActiveMode();

        boolean isCurrentBreathPhaseInspiratory = vtr.getActiveMode().getIsInInspiratoryPhase();
        if (isPreviousBreathPhaseInspiratory && !isCurrentBreathPhaseInspiratory) {
            didPhaseTransitionFromInspToExp = true;
        } else {
            didPhaseTransitionFromInspToExp = false;
        }

        if (!isPreviousBreathPhaseInspiratory && isCurrentBreathPhaseInspiratory) {
            didPhaseTransitionFromExpToInsp = true;
        } else {
            didPhaseTransitionFromExpToInsp = false;
        }
        updateMetricsMap();
        isPreviousBreathPhaseInspiratory = isCurrentBreathPhaseInspiratory;
    }

    public Metric getMetric(MetricName metricName) {
        return metricsMap.get(metricName);
    }

    private void setMetricValue(MetricName metricName, float value) {
        metricsMap.get(metricName).setValue(value);
    }
}

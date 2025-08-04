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
    private float sumMeasuredPeepTimeIntegrals;
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
    private float meanPeep;
    private int totalBreathsDuringCurrentPeriod;
    private float timeSinceLastUpdateOfExhMV;
    private float updateWindow = 5.0f;
    private float nextUpdateTime = updateWindow;
    private float previousSystemPressure;
    private float totalExhaledTidalVolumeDuringCurrentPeriod;
    private float timeSinceLastUpdateOfMeanRespRate;

    // Add separate update windows
    private float respRateUpdateWindow = 5.0f;
    private float minuteVolumeUpdateWindow = 5.0f;
    private float nextRespRateUpdateTime = respRateUpdateWindow;
    private float nextMinuteVolumeUpdateTime = minuteVolumeUpdateWindow;
    private float previousBreathInspiredTidalVolume;
    private float previousBreathExpiredTidalVolume;

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
        updateExhaledMinuteVolume();
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
        }

        nextPeakPressure = Math.max(metricsMap.get(MetricName.PEAK_PRESSURE).getValue(),
                vtr.getPressureSensor().getCurrentSystemPressure());

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
        }

        sumPressureTimeIntegrals += (vtr.getPressureSensor().getCurrentSystemPressure()
                * (vtr.getActiveMode().getTickPeriod() / 1000f));
    }

    /**
     * Updates the mean pressure in the metrics map only when the breath phase
     * transitions from expiration to inspiration. Does nothing during the
     * inspiratory phase. During the expiratory phase, keeps a running sum of
     * pressure-time integrals which are divided by the total time spent in the
     * expiratory phase when the mean peep gets updated.
     */
    // public void updateMeanPeep() {
    // if (vtr.getActiveMode().getIsInInspiratoryPhase() &&
    // didPhaseTransitionFromExpToInsp) {
    // float expPhaseTime = controller.getTimeInPreviousExpPhase();
    // setMetricValue(MetricName.MEAN_PEEP,
    // expPhaseTime != 0 ? sumMeasuredPeepTimeIntegrals / expPhaseTime : 0);
    // sumMeasuredPeepTimeIntegrals = 0;
    // }

    // if (vtr.getActiveMode().getIsInInspiratoryPhase()) {
    // return;
    // } else {
    // sumMeasuredPeepTimeIntegrals +=
    // (vtr.getPressureSensor().getCurrentSystemPressure()
    // * (vtr.getActiveMode().getTickPeriod() / 1000f));
    // }
    // }

    public void updateMeanPeep() {
        if (vtr.getActiveMode().getIsInInspiratoryPhase() && didPhaseTransitionFromExpToInsp) {
            setMetricValue(MetricName.MEAN_PEEP, previousSystemPressure);
        }
    }

    private float updateValuePerCondition(float updateAmount, boolean updateCondition) {
        if (updateCondition) {
            return updateAmount;
        } else {
            return 0;
        }
    }

    /**
     * Updates the mean respiratory rate in the metrics map every 5s. Keeps track of
     * the respiratory rate for the previous 5s.
     * 
     * TODO: there's a problem with this method. Test isn't passing
     */
    public void updateMeanRespRate() {
        if (didPhaseTransitionFromExpToInsp) {
            totalBreathsDuringCurrentPeriod++;
        }

        if (hasRespRateUpdateWindowElapsed(controller.getVentilationTimeForActiveMode())) {
            setMetricValue(MetricName.MEAN_RESP_RATE,
                    timeSinceLastUpdateOfMeanRespRate != 0
                            ? calculateAvgPerMin(totalBreathsDuringCurrentPeriod, timeSinceLastUpdateOfMeanRespRate)
                            : 0);
            totalBreathsDuringCurrentPeriod = 0;
            timeSinceLastUpdateOfMeanRespRate = 0;
        } else {
            timeSinceLastUpdateOfMeanRespRate += (controller.getTickPeriodInMs() / 1000f);
        }
    }

    public void updateExhaledMinuteVolume() {
        if (!controller.getActiveMode().getIsInInspiratoryPhase()) {
            totalExhaledTidalVolumeDuringCurrentPeriod += currentSystemVolumeChange;
        }
        if (hasMinuteVolumeUpdateWindowElapsed(controller.getVentilationTimeForActiveMode())) {
            setMetricValue(MetricName.EXHALED_MINUTE_VOLUME,
                    timeSinceLastUpdateOfExhMV != 0
                            ? calculateAvgPerMin(totalExhaledTidalVolumeDuringCurrentPeriod / 1000f, timeSinceLastUpdateOfExhMV)
                            : 0);
            totalExhaledTidalVolumeDuringCurrentPeriod = 0;
            timeSinceLastUpdateOfExhMV = 0;
        } else {
            timeSinceLastUpdateOfExhMV += (controller.getTickPeriodInMs() / 1000f);
        }
    }

    private float calculateAvgPerMin(float quantity, float timeSinceLastUpdate) {
        return (quantity / timeSinceLastUpdate) * 60;
    }

    // Create separate methods for checking update windows
    private boolean hasRespRateUpdateWindowElapsed(float ventilationTime) {
        if (ventilationTime >= nextRespRateUpdateTime) {
            nextRespRateUpdateTime += respRateUpdateWindow;
            return true;
        }
        return false;
    }

    private boolean hasMinuteVolumeUpdateWindowElapsed(float ventilationTime) {
        if (ventilationTime >= nextMinuteVolumeUpdateTime) {
            nextMinuteVolumeUpdateTime += minuteVolumeUpdateWindow;
            return true;
        }
        return false;
    }

    /**
     * 
     * Updates the inhaled tidal volume from the most recent inspiratory phase
     */
    public void updateMeasuredInhaledTidalVolume() {
        if (!didPhaseTransitionFromInspToExp) {
            return;
        } else {
            setMetricValue(MetricName.MEASURED_INHALED_TIDAL_VOLUME, previousBreathInspiredTidalVolume);
        }
    }

    /**
     * 
     * Updates the exhaled tidal volume from the most recent inspiratory phase
     */
    public void updateMeasuredExhaledTidalVolume() {
        if (!didPhaseTransitionFromExpToInsp) {
            return;
        } else {
            setMetricValue(MetricName.MEASURED_EXHALED_TIDAL_VOLUME, previousBreathExpiredTidalVolume);
        }
    }

    /**
     * 
     * Updates the measured inspired tidal volume from the last breath divided by
     * the peak inspiratory pressure minus the PEEP
     */
    public void updateDynamicCompliance() {
        if (!didPhaseTransitionFromInspToExp) {
            return;
        } else {
            float inspPressureChange = getMetric(MetricName.PEAK_PRESSURE).getValue() - getMetric(MetricName.MEAN_PEEP).getValue();
            setMetricValue(MetricName.DYNAMIC_COMPLIANCE,
                    inspPressureChange != 0 ? previousBreathInspiredTidalVolume / inspPressureChange : 0);
        }
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
        if (didPhaseTransitionFromInspToExp) {
            previousBreathInspiredTidalVolume = currentSystemVolumeChange;
        }

        if (didPhaseTransitionFromExpToInsp) {
            previousBreathExpiredTidalVolume = previousBreathInspiredTidalVolume - currentSystemVolumeChange;
        }
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

    /**
     * This method is responsible for updating all of the metrics up to and
     * including the tick period that just occurred.
     */
    public void update() {
        didPhaseTransitionFromExpToInsp = controller.getDidPhaseTransitionFromExpToInsp();
        didPhaseTransitionFromInspToExp = controller.getDidPhaseTransitionFromInspToExp();
        previousSystemPressure = this.currentSystemPressure;
        this.currentSystemPressure = vtr.getPressureSensor().getCurrentSystemPressure();
        this.currentSystemFlowrate = getCurrentSystemFlowrate();
        updateCurrentSystemVolumeChange();
        this.currentSystemTime = controller.getVentilationTimeForActiveMode();

        boolean isCurrentBreathPhaseInspiratory = vtr.getActiveMode().getIsInInspiratoryPhase();

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

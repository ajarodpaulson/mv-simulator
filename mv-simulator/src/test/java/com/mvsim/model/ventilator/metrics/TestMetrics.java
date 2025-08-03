package com.mvsim.model.ventilator.metrics;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mvsim.model.SimulationManager;
import com.mvsim.model.exception.ActiveModeNotSetException;
import com.mvsim.model.exception.NoSuchVentilationSettingException;
import com.mvsim.model.lungsim.LungSimSetting;
import com.mvsim.model.ventilator.VentilatorController;
import com.mvsim.model.ventilator.settings.FiO2;
import com.mvsim.model.ventilator.settings.InspiratoryTime;
import com.mvsim.model.ventilator.settings.Peep;
import com.mvsim.model.ventilator.settings.RespiratoryRate;
import com.mvsim.model.ventilator.settings.TidalVolume;

public class TestMetrics {
    SimulationManager simMgr;
    VentilatorController controller;

    @BeforeEach
    public void setup() throws ActiveModeNotSetException, NoSuchVentilationSettingException {
        this.simMgr = SimulationManager.getInstance();
        simMgr.reset();
        this.controller = simMgr.getVtrController();
        controller.setActiveModeSetting(InspiratoryTime.NAME, 1.0f);
        controller.setActiveModeSetting(TidalVolume.NAME, 500f);
        controller.setActiveModeSetting(FiO2.NAME, 50f);
        controller.setActiveModeSetting(RespiratoryRate.NAME, 20f);
        controller.setActiveModeSetting(Peep.NAME, 5.0f);
        simMgr.setLungSimSetting(LungSimSetting.COMPLIANCE, 100f);
        simMgr.setLungSimSetting(LungSimSetting.RESISTANCE, 1.0f);
        controller.enableVentilation();

    }

    @Test
    public void testUpdatePeakPressure() {
        assertEquals(0f, controller.getMetric(MetricName.PEAK_PRESSURE).getValue());

        while (controller.getActiveMode().getIsInInspiratoryPhase()) {
            assertEquals(0f, controller.getMetric(MetricName.PEAK_PRESSURE).getValue()); 
            controller.tick();
        }

        while (!controller.getActiveMode().getIsInInspiratoryPhase()) {
            assertEquals(10.5f, controller.getMetric(MetricName.PEAK_PRESSURE).getValue()); 
            controller.tick();
        }

        assertEquals(10.5f, controller.getMetric(MetricName.PEAK_PRESSURE).getValue());
    }

    @Test
    public void testUpdateMeanPressure() {
        assertEquals(0f, controller.getMetric(MetricName.MEAN_PRESSURE).getValue());

        while (controller.getActiveMode().getIsInInspiratoryPhase()) {
            assertEquals(0f, controller.getMetric(MetricName.MEAN_PRESSURE).getValue()); 
            controller.tick();
        }

        while (!controller.getActiveMode().getIsInInspiratoryPhase()) {
            assertEquals(0f, controller.getMetric(MetricName.MEAN_PRESSURE).getValue()); 
            controller.tick();
        }

        // ensure MAP is between PEEP and PIP for 20 breaths
        for (int i=0; i<20; i++) {
            while (controller.getActiveMode().getIsInInspiratoryPhase()) {
            System.out.println(controller.getMetric(MetricName.MEAN_PRESSURE).getValue());
            assertTrue(controller.getMetric(MetricName.MEAN_PRESSURE).getValue() >= 5.0f);
            assertTrue(controller.getMetric(MetricName.MEAN_PRESSURE).getValue() <= 10.5f);
            controller.tick();
        }

        while (!controller.getActiveMode().getIsInInspiratoryPhase()) {
            assertTrue(controller.getMetric(MetricName.MEAN_PRESSURE).getValue() >= 5.0f);
            assertTrue(controller.getMetric(MetricName.MEAN_PRESSURE).getValue() <= 10.5f);
            controller.tick();
        }
        }
    }
}

package com.mvsim.model.ventilator.mode;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mvsim.TestConstants;
import com.mvsim.model.SimulationManager;
import com.mvsim.model.exception.ActiveModeNotSetException;
import com.mvsim.model.exception.NoSuchVentilationSettingException;
import com.mvsim.model.lungsim.LungSim;
import com.mvsim.model.lungsim.LungSimSetting;
import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.VentilatorController;
import com.mvsim.model.ventilator.metrics.Metrics;
import com.mvsim.model.ventilator.settings.FiO2;
import com.mvsim.model.ventilator.settings.InspiratoryTime;
import com.mvsim.model.ventilator.settings.Peep;
import com.mvsim.model.ventilator.settings.RespiratoryRate;
import com.mvsim.model.ventilator.settings.TidalVolume;

public class VcCmvSetpointTest {
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
    public void testConstructor() {
        assertEquals(0, controller.getActiveMode().getTickCounter());
        assertEquals(0, controller.getActiveMode().getTimeInPhaseInMS());
        assertTrue(controller.getActiveMode().getIsInInspiratoryPhase());
    }

    @Test
    public void testFirstInspiratoryPhase() {
        int numberOfTicksInspPhase = ((int) (controller.getActiveMode().getSetting(InspiratoryTime.NAME).getValue()
                .floatValue() * 1000f)) / VentilationMode.TICK_PERIOD_IN_MS;
        Metrics metrics;

        assertEquals(5.0f, controller.getMetrics().getCurrentSystemPressure());

        for (int i = 1; i <= numberOfTicksInspPhase; i++) {
            controller.tick();
            metrics = controller.getMetrics();
            float pressureChangeFromAddedVolume = 25f / 100f;
            float pressureFromPeep = 5.0f;
            float pressureFromResistance = 1.0f * 0.5f;
            assertEquals(pressureChangeFromAddedVolume * i + pressureFromPeep + pressureFromResistance,
                    metrics.getCurrentSystemPressure(), TestConstants.TOL);
            assertEquals(30f, metrics.getCurrentSystemFlowrate(), TestConstants.TOL);
            assertEquals(25 * i, metrics.getCurrentSystemVolumeChange(), TestConstants.TOL);
        }
    }

    @Test
    public void testTransitionInspiratoryToExpiratory() {
        testFirstInspiratoryPhase();
        assertTrue(controller.getActiveMode().getIsInInspiratoryPhase());
        // pressure
        assertEquals(5.0f + 1.0 * 0.5f + 5.0f, controller.getMetrics().getCurrentSystemPressure(), TestConstants.TOL);
        // flow
        assertEquals(30f, controller.getMetrics().getCurrentSystemFlowrate(), TestConstants.TOL);
        // volume
        assertEquals(500f, controller.getMetrics().getCurrentSystemVolumeChange(), TestConstants.TOL);

        controller.tick();

        assertFalse(controller.getActiveMode().getIsInInspiratoryPhase());

        assertEquals(10.5, controller.getMetrics().getCurrentSystemPressure(), TestConstants.TOL);
        assertEquals(-60, controller.getMetrics().getCurrentSystemFlowrate(), TestConstants.TOL); // should be a
                                                                                                  // negative value
        assertEquals(450, controller.getMetrics().getCurrentSystemVolumeChange(), TestConstants.TOL);
    }

    @Test
    public void testExpiratoryPhase() {
        testFirstInspiratoryPhase();
        float iTime = controller.getActiveMode().getSetting(InspiratoryTime.NAME).getValue().floatValue() * 1000f;
        float breathCycleTime = (60 * 1000f)
                / controller.getActiveMode().getSetting(RespiratoryRate.NAME).getValue().floatValue();
        float eTime = breathCycleTime - iTime;
        int numberOfTicksExpPhase = (int) (eTime / VentilationMode.TICK_PERIOD_IN_MS); // should be 40
        Metrics metrics;

        float staticLungPressureAfterInspPhase = 10f;
        float systemVolumeChangeAfterInspPhase = controller.getMetrics().getCurrentSystemVolumeChange();
        float volumeChangePerTick = 50f;
        float pressureChangeFromExhaledVolume = volumeChangePerTick / 100f;
        float pressureFromResistance = 1.0f * 1.0f;
        
        // pressure equilibration phase
        for (int i = 1; i <= 10; i++) {
            controller.tick();
            metrics = controller.getMetrics();
            assertEquals(staticLungPressureAfterInspPhase + pressureFromResistance - pressureChangeFromExhaledVolume * i,
                    metrics.getCurrentSystemPressure(), TestConstants.TOL);
            assertEquals(-60f, metrics.getCurrentSystemFlowrate(), TestConstants.TOL);
            assertEquals(systemVolumeChangeAfterInspPhase - volumeChangePerTick * i,
                    metrics.getCurrentSystemVolumeChange(), TestConstants.TOL);
        }

        // now we enter the zero flow phase
        for (int i = 1; i <= numberOfTicksExpPhase - 10; i++) {
            controller.tick();
            metrics = controller.getMetrics();
            assertEquals(5.0f,
                    metrics.getCurrentSystemPressure(), TestConstants.TOL);
            assertEquals(0f, metrics.getCurrentSystemFlowrate(), TestConstants.TOL);
            assertEquals(0f, metrics.getCurrentSystemVolumeChange(),
                    TestConstants.TOL);
        }
    }

    @Test
    public void testTransitionExpiratoryToInspiratory() {
        testExpiratoryPhase();

        assertFalse(controller.getActiveMode().getIsInInspiratoryPhase());
        // pressure
        assertEquals(5.0f, controller.getMetrics().getCurrentSystemPressure(), TestConstants.TOL);
        // flow
        assertEquals(0f, controller.getMetrics().getCurrentSystemFlowrate(), TestConstants.TOL);
        // volume
        assertEquals(0f, controller.getMetrics().getCurrentSystemVolumeChange(), TestConstants.TOL);

        controller.tick();

        assertTrue(controller.getActiveMode().getIsInInspiratoryPhase());

        assertEquals(5.75f, controller.getMetrics().getCurrentSystemPressure(), TestConstants.TOL);
        assertEquals(30f, controller.getMetrics().getCurrentSystemFlowrate(), TestConstants.TOL); 
        assertEquals(25f, controller.getMetrics().getCurrentSystemVolumeChange(), TestConstants.TOL);
    }
}

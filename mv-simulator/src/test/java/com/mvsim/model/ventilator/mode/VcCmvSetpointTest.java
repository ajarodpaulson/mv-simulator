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
        controller.setActiveModeSetting(RespiratoryRate.NAME, 16f);
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
    public void testInspiratoryPhase() {
        int numberOfTicksInspPhase = ((int) (controller.getActiveMode().getSetting(InspiratoryTime.NAME).getValue().floatValue() * 1000f)) / VentilationMode.TICK_PERIOD_IN_MS;

        Metrics metrics;
        for (int i=1; i<=numberOfTicksInspPhase; i++) {
            controller.tick();
            metrics = controller.getMetrics();
            float pAlv = 25f / 100f + 5.0f;
            float pAw = 1.0f * 0.5f + pAlv;
            assertEquals(pAw * i, metrics.getCurrentSystemPressure());
            assertEquals(30f, metrics.getCurrentSystemFlowrate(), TestConstants.TOL);
            assertEquals(25 * i, metrics.getCurrentSystemVolumeChange(), TestConstants.TOL);
        }



    }

    @Test
    public void testTransitionInspiratoryToExpiratory() {

    }

    @Test
    public void testExpiratoryPhase() {

    }

    @Test
    public void testTransitionExpiratoryToInspiratory() {

    }
}

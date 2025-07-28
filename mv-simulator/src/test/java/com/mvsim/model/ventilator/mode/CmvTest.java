package com.mvsim.model.ventilator.mode;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mvsim.model.exception.ActiveModeNotSetException;
import com.mvsim.model.lungsim.LungSim;
import com.mvsim.model.ventilator.Ventilator;
import com.mvsim.model.ventilator.settings.InspiratoryTime;
import com.mvsim.model.ventilator.settings.RespiratoryRate;

public class CmvTest {
    Ventilator vtr;
    VentilationMode<?> mode;
    Cmv cmv;
    float TOL = 0.001f;

    @BeforeEach
    public void setup() {
        this.vtr = new Ventilator();
        vtr.setLungSim(new LungSim());
        vtr.getController()
                .setActiveModeWithCorrespondingDefaultSettings(new ModeTAG(ControlVariableType.VOLUME_CONTROL,
                        BreathSequenceType.CONTINUOUS_MANDATORY_VENTILATION, TargetingSchemeType.SET_POINT));
        try {
            vtr.enableVentilation();
        } catch (ActiveModeNotSetException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testConstructor() {
        assertTrue(vtr.getController().getActiveMode().getIsInInspiratoryPhase());
    }

    // TODO: I think there are some one-off errors here wrtt breath phase
    // transitions... you need to break down the tests
    @Test
    public void testDeterminePhase() {
        // assume that Itime is a multiple of VentilationMode.TICK_PERIOD_IN_MS
        float iTime = (float) vtr.getController().getActiveMode().getSettings().getSetting(InspiratoryTime.NAME)
                .getValue();
        int respRate = (int) vtr.getController().getActiveMode().getSettings().getSetting(RespiratoryRate.NAME)
                .getValue();
        float breathCycleDuration = 60f / respRate;
        float eTime = breathCycleDuration - iTime;

        int numberOfTicksInspPhase = (int) (iTime / (VentilationMode.TICK_PERIOD_IN_MS / 1000f));
        int numberOfTicksExpPhase = (int) (eTime / (VentilationMode.TICK_PERIOD_IN_MS / 1000f));
        // the tick counter only increments when the control completes
        for (int t = 0; t < numberOfTicksInspPhase; t++) {
            vtr.getController().tick();
            assertEquals((t+1) * VentilationMode.TICK_PERIOD_IN_MS,
                    vtr.getController().getActiveMode().getTimeInPhaseInMS());
            assertTrue(vtr.getController().getActiveMode().getIsInInspiratoryPhase());
        }
        assertEquals(iTime * 1000f, (float) vtr.getController().getActiveMode().getTimeInPhaseInMS(), TOL);
        // the next tick will transition us into the expiratory phase... so we need to
        // start the iteration variable at 0 to account for the fact that before the
        // first execution of the iteration body, we are still in inspiration
        for (int t = 0; t < numberOfTicksExpPhase; t++) {
            vtr.getController().tick();
            assertFalse(vtr.getController().getActiveMode().getIsInInspiratoryPhase());
        }
        assertEquals(eTime * 1000f, (float) vtr.getController().getActiveMode().getTimeInPhaseInMS(), TOL);

        vtr.getController().tick();
        assertTrue(vtr.getController().getActiveMode().getIsInInspiratoryPhase());
    }
}

package com.mvsim.model.ventilator.mode;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.mvsim.model.ventilator.Ventilator;

public class CmvTest {
    Ventilator vtr;
    VentilationMode<?> mode;
    Cmv cmv;

    @BeforeEach
    public void setup() {
        this.vtr = new Ventilator();
        vtr.getController().setActiveMode(new ModeTAG(ControlVariableType.VOLUME_CONTROL, BreathSequenceType.CONTINUOUS_MANDATORY_VENTILATION, TargetingSchemeType.SET_POINT));
        VcCmvSetpointSettings vcCmvSetpointSettings = new VcCmvSetpointSettings();
        vcCmvSetpointSettings.getSetting(null)
        vtr.getController().getActiveMode().setSettings(vcCmvSetpointSettings);
        this.cmv = new Cmv(vtr);
    }

    @Test
    public void testConstructor() {
        
    }

    @Test
    public void determinePhaseGivenPrevPhaseInspAndTimeRemShouldChooseInsp() {

    }
}

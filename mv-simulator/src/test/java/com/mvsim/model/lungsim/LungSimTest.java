package com.mvsim.model.lungsim;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mvsim.model.ventilator.mode.VentilationMode;

public class LungSimTest {
    private LungSim ls;

    @BeforeEach
    public void setup() {
        ls = new LungSim(100f, 0.5f);
    }

    @Test
    public void testConstructor() {
        assertEquals(100f, ls.getCompliance());
        assertEquals(0.5f, ls.getResistance());
    }

    @Test
    public void testGetTimeConstant() {
        assertEquals(100f * (0.5f / 1000), ls.getTimeConstant());
    }

    @Test void testGetPressureChangePerTick() {
        assertEquals(ls.getPressureChangePerTick(0f), 0);
        
        assertEquals(15f * ((VentilationMode.TICK_PERIOD_IN_MS / 1000f) / (5*ls.getTimeConstant())), ls.getPressureChangePerTick(15));
        // shouldn't change
        assertEquals(15f * ((VentilationMode.TICK_PERIOD_IN_MS / 1000f) / (5*ls.getTimeConstant())), ls.getPressureChangePerTick(15));
        // should change
        assertEquals(5f * ((VentilationMode.TICK_PERIOD_IN_MS / 1000f) / (5*ls.getTimeConstant())), ls.getPressureChangePerTick(5));
    }
}

package com.mvsim.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UtilitiesTest {
    /*
     * v1 > v2, within and not within
     * v1 < v2, within and not within
     * v1 == v2
     * negative values
     * mixing negative and positive values
     */
    @Test
    public void testEqualWithinTolerance() {
        assertTrue(Utilities.equalWithinTolerance(4.99f, 5.00f));
        assertFalse(Utilities.equalWithinTolerance(4.98f, 5.00f));

        assertTrue(Utilities.equalWithinTolerance(5.01f, 5.00f));
        assertFalse(Utilities.equalWithinTolerance(5.02f, 5.00f));

        assertTrue(Utilities.equalWithinTolerance(5.00f, 5.00f));

        assertTrue(Utilities.equalWithinTolerance(-0.01f, 0.00f));
        assertFalse(Utilities.equalWithinTolerance(-0.02f, 0.00f));
    }

    
}

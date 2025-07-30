package com.mvsim.model;

public class Utilities {
    public static float TOL = 0.01f;
    public static boolean equalWithinTolerance(float v1, float v2) {
        if (v1 == v2) {
            return true;
        }
        else if (v1 < v2) {
            return v1 + TOL >= v2;
        } else {
            return v1 - TOL <= v2;
        }

    }
}

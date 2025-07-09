package com.mvsim.model.ventilator;

public class VcCmvSetpoint extends VentilationMode<VolumeBased> {
    VcCmvSetpoint(Ventilator vtr) {
        super(vtr, new VolumeControl(), new Cmv(), new VolumeSetPoint(), new VcCmvSetpointSettings());
    }
}
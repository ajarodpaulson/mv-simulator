package com.mvsim.model.ventilator;

public class VcCmvSetpoint extends VentilationMode<VolumeBased> {

    VcCmvSetpoint(Ventilator vtr) {
        super(vtr, new Cmv(), new VolumeSetPoint(), new VolumeControl());
        //TODO Auto-generated constructor stub
    }
}
package com.mvsim.ui.desktop;

import java.awt.Container;
import java.util.function.Consumer;

import javax.swing.JSpinner;

import com.mvsim.model.SimulationManager;

public class VentilatorSettingsButton extends SettingsButton {

    VentilatorSettingsButton(Container c, String label, float value, float min, float max,
            float stepSize) {
        super(c, new Consumer<JSpinner>() {

            @Override
            public void accept(JSpinner spinner) {
                SimulationManager.getInstance().getVtrController().setModeSetting(label,
                        ((Number) spinner.getValue()).floatValue());
            }

        }, label, value, min, max, stepSize);
    }

}

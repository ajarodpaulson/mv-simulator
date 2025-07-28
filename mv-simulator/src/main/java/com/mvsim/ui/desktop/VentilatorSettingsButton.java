package com.mvsim.ui.desktop;

import java.awt.Container;
import java.util.function.Consumer;

import javax.swing.JSpinner;

import com.mvsim.model.SimulationManager;
import com.mvsim.model.exception.NoSuchVentilationSettingException;

public class VentilatorSettingsButton extends SettingsButton {

    VentilatorSettingsButton(Container c, String label, float value, float min, float max,
            float stepSize) {
        super(c, new Consumer<JSpinner>() {

            @Override
            public void accept(JSpinner spinner) {
                try {
                    SimulationManager.getInstance().getVtrController().setActiveModeSetting(label,
                            ((Number) spinner.getValue()).floatValue());
                } catch (NoSuchVentilationSettingException e) {
                    System.out.println(e.getMessage());
                }
            }

        }, label, value, min, max, stepSize);
    }

}

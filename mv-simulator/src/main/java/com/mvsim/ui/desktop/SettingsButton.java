package com.mvsim.ui.desktop;

import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class SettingsButton extends JSpinner {
    SettingsButton() {
        super(new SpinnerNumberModel(value, min, max, stepSize));
    }
}

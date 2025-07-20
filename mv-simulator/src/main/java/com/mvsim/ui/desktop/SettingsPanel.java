package com.mvsim.ui.desktop;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public SettingsPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 1.0;
        add(new LungSimSettingsPanel(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        add(new VentilatorSettingsPanel(), gbc);
    }
}
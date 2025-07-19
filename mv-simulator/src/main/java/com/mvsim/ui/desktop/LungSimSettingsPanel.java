package com.mvsim.ui.desktop;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Map.Entry;

import javax.swing.JPanel;

import com.mvsim.model.LungSimSetting;
import com.mvsim.model.SimulationManager;

public class LungSimSettingsPanel extends JPanel {
    LungSimSettingsPanel() {
        setBackground(Color.RED);
        setLayout(new GridLayout(0, 2, 1, 0));
        for (Entry<LungSimSetting, Number> setting : SimulationManager.getInstance().getLungSim().getSettings()) {
            
        }
    }
}

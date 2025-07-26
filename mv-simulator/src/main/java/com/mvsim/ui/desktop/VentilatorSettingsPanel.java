package com.mvsim.ui.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.mvsim.model.SimulationManager;
import com.mvsim.model.ventilator.settings.Setting;

//  XXX: semantic coupling/duplication between this and LungSimSettingsPanel
public class VentilatorSettingsPanel extends JPanel {
    VentilatorSettingsPanel() {
        setBackground(Color.BLUE);
        
        // Add a red border around this entire panel
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 2)); // 2-pixel red border
        
        setLayout(new GridLayout(1, 2, 1, 0));
        for (Setting setting : SimulationManager.getInstance().getVtrController().getActiveMode().getSettings()) {
            float value = setting.getValue().floatValue();
            String name = setting.getName();
            JPanel buttonContainer = new JPanel();
            buttonContainer.setLayout(new BorderLayout());
            new VentilatorSettingsButton(buttonContainer, name, value, setting.getMin(), setting.getMax(), setting.getStepSize());
            add(buttonContainer);
        }
    }
}

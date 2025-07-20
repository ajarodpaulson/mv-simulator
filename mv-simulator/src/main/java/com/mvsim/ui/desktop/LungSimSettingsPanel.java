package com.mvsim.ui.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import com.mvsim.model.SimulationManager;
import com.mvsim.model.lungsim.LungSimSetting;

public class LungSimSettingsPanel extends JPanel {
    LungSimSettingsPanel() {
        setBackground(Color.RED);
        
        // Add a red border around this entire panel
        setBorder(BorderFactory.createLineBorder(Color.RED, 2)); // 2-pixel red border
        
        setLayout(new GridLayout(1, 2, 1, 0));
        for (Entry<LungSimSetting, Number> setting : SimulationManager.getInstance().getLungSim().getSettings()) {
            float value = setting.getValue().floatValue();
            LungSimSetting settingContext = setting.getKey();
            JPanel buttonContainer = new JPanel();
            buttonContainer.setLayout(new BorderLayout());
            new LungSimSettingsButton(buttonContainer, settingContext.getName(), value, settingContext.getMin(), settingContext.getMax(), settingContext.getStepSize());
            add(buttonContainer);
        }
    }
}

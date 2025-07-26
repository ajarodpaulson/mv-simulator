package com.mvsim.ui.desktop;

import javax.swing.*;

import com.mvsim.model.SimulationManager;
import com.mvsim.model.ventilator.mode.ModeTAG;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class InfoPanel extends JPanel {
    private JComboBox<ModeTAG> modePicker;

    public InfoPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 8));
        setBackground(Color.BLACK);

        modePicker = new JComboBox<>();

        // Get available modes and add them directly
        Set<ModeTAG> availableModes = SimulationManager.getInstance().getVtrController().getAvailableModes();
        for (ModeTAG mode : availableModes) {
            modePicker.addItem(mode);
        }

        modePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModeTAG selectedMode = (ModeTAG) modePicker.getSelectedItem();
                SimulationManager.getInstance()
                    .getVtrController()
                    .setActiveModeWithCorrespondingDefaultSettings(selectedMode);
            }
        });
        add(modePicker);
    }
}
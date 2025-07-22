package com.mvsim.ui.desktop;

import javax.swing.*;

import com.mvsim.model.SimulationManager;
import com.mvsim.model.exception.ActiveModeNotSetException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    public MenuPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        AbstractButton btn = new JToggleButton("Start Ventilation");
        
        // Fix button colors
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setBackground(Color.GREEN);
        btn.setForeground(Color.BLACK);
        
        // Simple fixed width approach - adjust as needed
        Dimension buttonSize = new Dimension(150, 30); // XXX hard coded 
        btn.setPreferredSize(buttonSize);
        btn.setMinimumSize(buttonSize);
        btn.setMaximumSize(buttonSize);
        
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusable(false);
        add(btn);
        add(Box.createRigidArea(new Dimension(0, 8)));
        
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btn.isSelected()) {
                    try {
                        SimulationManager.getInstance().startSimulation();
                        btn.setText("Stop Ventilation");
                        btn.setBackground(Color.RED);
                        btn.setForeground(Color.WHITE);
                    } catch (ActiveModeNotSetException e1) {
                        System.out.println(e1.getMessage());
                    }
                } else {
                    SimulationManager.getInstance().stopSimulation();
                    btn.setText("Start Ventilation");
                    btn.setBackground(Color.GREEN);
                    btn.setForeground(Color.BLACK);
                }
            }
        };
        btn.addActionListener(actionListener);
    }
}
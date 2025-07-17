package com.mvsim.ui.desktop;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        String[] labels = {
            "Standby", "Disconnection", "Modes",
            "Alarm Limits", "Trends & Logs",
            "Maneuvers", "Views", "Lock Screen"
        };

        for (String text : labels) {
            JButton btn = new JButton(text);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(140, 30));
            btn.setFocusable(false);
            add(btn);
            add(Box.createRigidArea(new Dimension(0, 8)));
        }
    }
}
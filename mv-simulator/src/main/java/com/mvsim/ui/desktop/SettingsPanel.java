package com.mvsim.ui.desktop;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public SettingsPanel() {
        setLayout(new GridLayout(1, 5, 12, 0));
        setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        setBackground(Color.BLACK);

        add(createSetting("O₂", 35, 0, 100, "%"));
        add(createSetting("PEEP", 8, 0, 20, "cmH₂O"));
        add(createSetting("Backup RR", 15, 0, 40, "bpm"));
        add(createSetting("Apnea Time", 20, 0, 60, "s"));
        add(createSetting("Trigger", -2, -5, 0, "cmH₂O"));
    }

    private JPanel createSetting(String label, int val, int min, int max, String unit) {
        JPanel p = new JPanel(new BorderLayout(2, 2));
        p.setBackground(Color.BLACK);

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setForeground(Color.LIGHT_GRAY);

        JSlider slider = new JSlider(min, max, val);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        slider.setBackground(Color.DARK_GRAY);

        JLabel valLbl = new JLabel(val + " " + unit, SwingConstants.CENTER);
        valLbl.setForeground(Color.WHITE);
        valLbl.setFont(valLbl.getFont().deriveFont(12f));

        p.add(lbl, BorderLayout.NORTH);
        p.add(slider, BorderLayout.CENTER);
        p.add(valLbl, BorderLayout.SOUTH);

        return p;
    }
}
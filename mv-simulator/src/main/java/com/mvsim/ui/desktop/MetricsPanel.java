package com.mvsim.ui.desktop;

import javax.swing.*;
import java.awt.*;

public class MetricsPanel extends JPanel {
    public MetricsPanel() {
        setLayout(new GridLayout(0, 2, 6, 6));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.DARK_GRAY);

        addMetric("Ppeak", "17 cmH₂O");
        addMetric("Pmean", "11 cmH₂O");
        addMetric("PEEP",  "7.6 cmH₂O");
        addMetric("RR",    "26 bpm");
        addMetric("Ti/Ttot", "0.39");
        addMetric("O₂ conc.", "35 %");
        addMetric("MV",    "14.1 L/min");
        addMetric("VTI",   "478 mL");
        addMetric("VTE",   "586 mL");
        addMetric("VT/PBW", "7.5 mL/kg");
        addMetric("Cdyn",  "105 mL/cmH₂O");
    }

    private void addMetric(String name, String value) {
        JLabel lblName = new JLabel(name + ":");
        lblName.setForeground(Color.WHITE);
        lblName.setFont(lblName.getFont().deriveFont(14f));

        JLabel lblVal = new JLabel(value);
        lblVal.setForeground(Color.GREEN.brighter());
        lblVal.setFont(lblVal.getFont().deriveFont(Font.BOLD, 16f));

        add(lblName);
        add(lblVal);
    }
}
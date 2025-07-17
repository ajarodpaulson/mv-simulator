package com.mvsim.ui.desktop;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JLabel timerLabel = new JLabel("00:02");
    private JLabel modeLabel  = new JLabel("PS/CPAP → PC");
    private JLabel weightLabel = new JLabel("PBW 78 kg");

    public InfoPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 8));
        setPreferredSize(new Dimension(0, 60));
        setBackground(Color.BLACK);

        timerLabel.setForeground(Color.YELLOW);
        timerLabel.setFont(timerLabel.getFont().deriveFont(16f));

        modeLabel.setForeground(Color.WHITE);
        modeLabel.setFont(modeLabel.getFont().deriveFont(Font.BOLD, 18f));

        weightLabel.setForeground(Color.LIGHT_GRAY);
        weightLabel.setFont(weightLabel.getFont().deriveFont(14f));

        add(timerLabel);
        add(modeLabel);
        add(weightLabel);
    }

    // Expose setters so your VentilatorController can update these in real time:
    public void setTimer(String t)   { timerLabel.setText(t); }
    public void setMode(String m)    { modeLabel.setText(m); }
    public void setWeight(String w)  { weightLabel.setText(w); }
}
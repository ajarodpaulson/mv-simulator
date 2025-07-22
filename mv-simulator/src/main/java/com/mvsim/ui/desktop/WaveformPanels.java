package com.mvsim.ui.desktop;

import javax.swing.*;

import com.mvsim.model.SimulationManager;

import java.awt.*;

public class WaveformPanels extends JPanel {

    private final PressureWaveformPanel pressureWaveformPanel;
    private final FlowWaveformPanel flowWaveformPanel;
    private final VolumeWaveformPanel volumeWaveformPanel;

    public WaveformPanels() {
        setLayout(new GridLayout(3, 1, 0, 1));
        setBackground(Color.BLACK);
        pressureWaveformPanel = new PressureWaveformPanel();
        flowWaveformPanel = new FlowWaveformPanel();
        volumeWaveformPanel = new VolumeWaveformPanel();
        WaveformPanel[] waveformPanels = {pressureWaveformPanel, flowWaveformPanel, volumeWaveformPanel};
        for (WaveformPanel p : waveformPanels) {
            SimulationManager.getInstance().addObserver(p);
        }
        add(pressureWaveformPanel);
        add(flowWaveformPanel);
        add(volumeWaveformPanel);
    }

    public PressureWaveformPanel getPressureWaveformPanel() {
        return pressureWaveformPanel;
    }

    public FlowWaveformPanel getFlowWaveformPanel() {
        return flowWaveformPanel;
    }

    public VolumeWaveformPanel getVolumeWaveformPanel() {
        return volumeWaveformPanel;
    }
}
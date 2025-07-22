package com.mvsim.ui.desktop;

import java.awt.Color;
import java.util.function.Function;

import javax.swing.JPanel;

import com.mvsim.model.Units;
import com.mvsim.model.ventilator.MostRecentTickData;

public class FlowWaveformPanel extends WaveformPanel {
    FlowWaveformPanel() {
        super("Flowrate", Units.FLOWRATE.getNotation(), Color.GREEN, new Function<MostRecentTickData, Float>() {

            @Override
            public Float apply(MostRecentTickData data) {
                return data.getCurrentSystemFlowrate();
            }
        });
    }
}

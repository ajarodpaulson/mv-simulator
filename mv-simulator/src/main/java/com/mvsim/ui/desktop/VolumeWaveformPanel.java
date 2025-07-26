package com.mvsim.ui.desktop;

import java.awt.Color;
import java.util.function.Function;

import javax.swing.JPanel;

import com.mvsim.model.Units;
import com.mvsim.model.ventilator.metrics.Metrics;
import com.mvsim.model.ventilator.metrics.MostRecentTickData;

public class VolumeWaveformPanel extends WaveformPanel {

        VolumeWaveformPanel() {
        super("Volume", Units.VOLUME.getNotation(), Color.BLUE, new Function<Metrics, Float>() {

            @Override
            public Float apply(Metrics metrics) {
                return metrics.getCurrentSystemVolumeChange();
            }
        });
    }
}

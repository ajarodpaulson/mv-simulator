package com.mvsim.ui.desktop;

import java.awt.Color;
import java.util.function.Function;

import javax.swing.JPanel;

import com.mvsim.model.Units;
import com.mvsim.model.ventilator.MostRecentTickData;

public class VolumeWaveformPanel extends WaveformPanel {

        VolumeWaveformPanel() {
        super("Volume", Units.VOLUME.getNotation(), Color.BLUE, new Function<MostRecentTickData, Float>() {

            @Override
            public Float apply(MostRecentTickData data) {
                return data.getCurrentSystemVolume();
            }
        });
    }

}

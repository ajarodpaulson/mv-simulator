package com.mvsim.ui.desktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.mvsim.model.Units;
import com.mvsim.model.ventilator.metrics.Metrics;
// import com.mvsim.model.ventilator.metrics.MostRecentTickData;

public final class PressureWaveformPanel extends WaveformPanel {
    PressureWaveformPanel() {
        super("Pressure", Units.PRESSURE.getNotation(), Color.YELLOW, new Function<Metrics, Float>() {

            @Override
            public Float apply(Metrics metrics) {
                return metrics.getCurrentSystemPressure();
            }
        });
    }
}

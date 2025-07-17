package com.mvsim.ui.desktop;

import javax.swing.*;
import java.awt.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class WaveformPanel extends JPanel {
    private final XYSeries pressureSeries;
    private final XYSeries flowSeries;
    private final XYSeries volumeSeries;

    public WaveformPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Create series for each waveform
        pressureSeries = new XYSeries("Pressure");
        flowSeries     = new XYSeries("Flow");
        volumeSeries   = new XYSeries("Volume");

        // Combine into one dataset
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(pressureSeries);
        dataset.addSeries(flowSeries);
        dataset.addSeries(volumeSeries);

        // Build the chart
        JFreeChart chart = ChartFactory.createXYLineChart(
            null,        // no title
            "Time (s)",  // X-axis label
            "Value",     // Y-axis label
            dataset      // the data
        );

        // Style the plot
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.BLACK);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);

        // Configure axes to auto-range
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setAutoRange(true);
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setAutoRange(true);

        // Set distinct colors per series
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.YELLOW);
        renderer.setSeriesPaint(1, Color.GREEN);
        renderer.setSeriesPaint(2, Color.CYAN);
        plot.setRenderer(renderer);

        // Wrap in a ChartPanel and add to this JPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Backend logic should call these methods whenever a new sample arrives.
     * The 'time' parameter could be a timestamp in seconds, or just a sample index.
     */
    public void addPressureSample(double time, double value) {
        pressureSeries.add(time, value);
    }

    public void addFlowSample(double time, double value) {
        flowSeries.add(time, value);
    }

    public void addVolumeSample(double time, double value) {
        volumeSeries.add(time, value);
    }
}